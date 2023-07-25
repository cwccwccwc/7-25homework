package zy.Internet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Test1_InetAdderss {
    public static void main(String[] args) throws IOException {
        InetAddress localHost = InetAddress.getLocalHost();
        System.out.println("本地的ip地址为："+localHost);

        InetAddress[] allByName = InetAddress.getAllByName("WWW.qingyunke.com");
        if (allByName!=null){
            for (InetAddress ip:allByName){
                System.out.println(ip);
                Socket socket = new Socket(ip, 80);
                System.out.println("链接成功"+socket);
                break;
            }
        }
    }
}
