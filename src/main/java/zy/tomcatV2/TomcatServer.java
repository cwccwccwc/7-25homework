package zy.tomcatV2;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import zy.tomcatV2.javax.YcWebServlet;
import zy.tomcatV2.javax.http.YcServletContext;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;

public class TomcatServer {
    static Logger logger = Logger.getLogger(TomcatServer.class);
    public static void main(String[] args) {
        logger.debug("服务器启动了");
        TomcatServer ts = new TomcatServer();
        logger.debug("服务器配置端口为:"+ts.parsePortFromXml());
        ts.startServer(ts.parsePortFromXml());
    }

    private void startServer(int port){
        boolean flag=true;

        String packageName="zy.tomcatV2";
        String packagePath = packageName.replaceAll("\\.", "/");
        //服务器启动时，扫描它所有的 classes，查找@YcWebServlet的class，存到map中
        //jvm类加载器
        try{
            Enumeration<URL> files = Thread.currentThread().getContextClassLoader().getResources(packagePath);
            while (files.hasMoreElements()){
                URL url=files.nextElement();
                logger.info("正在扫描的包路径为:"+url.getFile());
                //查找包下面的文件
                findPackagelasses(url.getFile(),packageName);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }


        try (ServerSocket ss = new ServerSocket(port)){
            logger.debug("服务器启动成功，配置窗口为："+port);
            //TODO：可以获取server.xml中关于是否开启线程的配置项  决定是否使用线程池
            while (flag){
                try {
                    Socket s = ss.accept();
                    logger.debug("客户端："+s.getRemoteSocketAddress()+"链接上了服务器");

                    TaskServer task = new TaskServer(s);
                    Thread t = new Thread(task);
                    t.start();
                }catch (Exception ex){
                    ex.printStackTrace();
                    logger.error("客户端连接失败...");
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
            logger.error("服务器套接字创建失败...");
        }
    }

    /*
    * packagePath  com/yc
    * packageName  com.yc
    * */
    private void findPackagelasses(String packagePath, String packageName) {
        if (packagePath.startsWith("/")){
            packagePath=packagePath.substring(1);
        }
//        ..去这个路径下所有的文件
        File file = new File(packagePath);
        File[] classFiles = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.getName().endsWith(".class") || pathname.isDirectory()) {
                    return true;
                } else {
                    return false;
                }
            }
        });
//        System.out.println(classFiles);
        if (classFiles!=null&&classFiles.length>0){
            for (File cf:classFiles){
                if (cf.isDirectory()){
                    findPackagelasses(cf.getAbsolutePath(),packageName+"."+cf.getName());
                }else {
                    //是字节码文件  则利用类加载器加载这个 class文件
                    URLClassLoader uc = new URLClassLoader(new URL[]{});
                    try {
                        Class cls = uc.loadClass(packageName + "." + cf.getName().replace(".class", ""));
                        if (cls.isAnnotationPresent(YcWebServlet.class)) {
                            logger.info("加载了一个类" + cls.getName());
                            YcWebServlet anno = (YcWebServlet) cls.getAnnotation(YcWebServlet.class);
                            String url = anno.value();
                            //通过 注解的value()方法取出 url地址 存到YcServletContext的map中
                            YcServletContext.servletClass.put(url,cls);
                        }
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }

            }
        }    }

    private int parsePortFromXml(){
        int port=8080;
        //方案一：根据 字节码的路径
//        TomcatServer.class.getClassLoader().getResourceAsStream()
//        方案二
        String serverXmlPath = System.getProperty("user.dir") + File.separator + "config" + File.separator + "server.xml";
        try(FileInputStream iis = new FileInputStream(serverXmlPath);
        ) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document doc = documentBuilder.parse(iis);
            NodeList nl = doc.getElementsByTagName("Connector");
            for (int i = 0; i < nl.getLength(); i++) {
                Element node = (Element) nl.item(i);
                port = Integer.parseInt(node.getAttribute("port"));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return port;
    }
}
