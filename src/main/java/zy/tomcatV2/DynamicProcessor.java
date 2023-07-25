package zy.tomcatV2;

import zy.tomcatV2.javax.YcServlet;
import zy.tomcatV2.javax.YcServletRequest;
import zy.tomcatV2.javax.YcServletResponse;
import zy.tomcatV2.javax.http.YcHttpServletRequest;
import zy.tomcatV2.javax.http.YcServletContext;

import java.io.PrintWriter;

public class DynamicProcessor implements Processor {

    @Override
    public void process(YcServletRequest request, YcServletResponse response) {
        //request中的参数已经解析好了

        //1.从request中取出requestURI( /hello ,  到ServletContext的map中去取class)
        String uri = ((YcHttpServletRequest) request).getRequestURI();
        int length = ((YcHttpServletRequest) request).getContextPath().length();
        uri = ((YcHttpServletRequest) request).getRequestURI().substring(length);

        YcServlet servlet=null;
        try {
            //2.为了保证单例，先看另一个map中是否已经有这个class的实例，如有，说明是第二次访问，则直接取，再调用service()
            if (YcServletContext.servletInstance.containsKey(uri)){
                servlet=YcServletContext.servletInstance.get(uri);
            }else {
                //   如果没有，则说明此servlet是第一次调用             先利用反射创建servlet             再调用init()-》service
                Class aClass = YcServletContext.servletClass.get(uri);
                Object obj = null;
                try {
                    obj = aClass.newInstance();
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (obj instanceof YcServlet){
                    servlet=(YcServlet) obj;
                    servlet.init();
                    YcServletContext.servletInstance.put(uri,servlet);
                }
            }
            servlet.service(request,response);
        }catch (Exception ex){
            String exc = ex.toString();
            String protocol=gen500(exc);
            //以输出流返回给客户端
            PrintWriter out = response.getPrintWriter();
            out.println(protocol);
            out.println(exc);
            out.flush();
        }

        //              还要考虑servelt执行失败的情况，则输出500错误 响应给客户端
    }
    private String gen500(String exc){
        String protocol500="HTTP/1.1 500 Internal Server Error\r\nContent-Type: text/html; charset=utf-8\r\nContent-length:"+exc.getBytes().length+"\r\n\r\n";
        return protocol500;
    }


}
