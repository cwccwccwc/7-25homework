package zy.Internet;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class Test8_url_urlconnection_httpurlconnection {
    public static void main(String[] args) throws IOException {
        // url类
        URL url = new URL("http://www.baidu.com");
        URLConnection con = url.openConnection();

        String contentType = con.getContentType();
        System.out.println(contentType);
        Object content = con.getContent();
        System.out.println(content);
    }
}
