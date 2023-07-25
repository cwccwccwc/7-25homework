package zy.ATM.V2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ATMServer {
    public static void main(String[] args) {
        int processors = Runtime.getRuntime().availableProcessors()*30;

        ServerSocket ss = null;
        try {
            ss = new ServerSocket(60000);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务器启动失败");
            return;
        }
        System.out.println("服务器:"+ss.getLocalSocketAddress()+"正常启动，监听"+ss.getLocalPort()+"端口");
        boolean flag = true;
        while (flag){
            Socket s = null;
            try {
                s = ss.accept();
                System.out.println("客户机"+s.getRemoteSocketAddress()+"联接到服务器");
                BankTask bt = new BankTask(s);
                Thread t = new Thread(bt);
                t.start();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("建立与和客户端的联接失败");
            }

        }

    }

}
