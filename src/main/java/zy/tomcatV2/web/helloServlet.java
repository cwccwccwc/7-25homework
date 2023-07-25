package zy.tomcatV2.web;

import zy.tomcatV2.javax.YcWebServlet;
import zy.tomcatV2.javax.http.YcHttpServlet;
import zy.tomcatV2.javax.http.YcHttpServletRequest;
import zy.tomcatV2.javax.http.YcHttpServletResponse;

import java.io.PrintWriter;

@YcWebServlet("/hello")
public class helloServlet extends YcHttpServlet {
    public helloServlet() {
        System.out.println("构造方法.....");
    }

    @Override
    public void init() {
        System.out.println("初始化.......");
    }

    @Override
    protected void doGet(YcHttpServletRequest req, YcHttpServletResponse resp) {
        System.out.println("hello Servlet  hello world");
        String result="野球帝王";
        PrintWriter out = resp.getPrintWriter();
        out.println("HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=utf-8\r\nContent-length:"+result.getBytes().length+"\r\n\r\n");
        out.println(result);
        out.flush();
    }
}
