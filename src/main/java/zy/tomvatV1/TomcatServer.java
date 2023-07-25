package zy.tomvatV1;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;

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
