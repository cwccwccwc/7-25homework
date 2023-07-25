package zy.Internet;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test3_server {
    public static void main(String[] args) {
        ServerSocket ss=null;
        System.out.println("服务器启动。。。");
        for (int i = 10000; i < 65535; i++) {
            try {
                ss=new ServerSocket(10001);
                System.out.println("服务器启动成功,占用"+ss.getLocalPort()+"端口");
            }catch (Exception e){
                continue;
            }
            break;
        }

        //服务器端是一直等待客户端连接
        Socket s=null;
        while (true){
            try {
                 s = ss.accept();
                System.out.println("有一个客户端："+s.getRemoteSocketAddress()+"连接上了服务器");
                //输出流
                OutputStream oos = s.getOutputStream();
                SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                String result="当前服务器时间为"+df.format(new Date());
                oos.write(result.getBytes());
                oos.flush();
            }catch (IOException e){
                    e.printStackTrace();
            }finally {
                try {
                    s.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }


}
