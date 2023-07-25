package zy.tomcatV2.javax.http;

import zy.tomcatV2.javax.YcServlet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class YcServletContext {
    /*
    * url地址，字节码路径
    *   ->再利用反射实例化成一个对象
    * */
    public static Map<String,Class> servletClass=new ConcurrentHashMap<String,Class>();

    /*
    * 每个servlet都是单例，当第一次访问这个servlet时，创建后保存到这个map中
    *
    * */
    public static Map<String, YcServlet> servletInstance=new ConcurrentHashMap<String,YcServlet>();
}
