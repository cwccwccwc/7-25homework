package zy.Internet;


import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Test3_client {
    public static void main(String[] args) {
        Socket s=null;
        try{
            s=new Socket("localhost",10001);
            System.out.println("连接成功"+s);
        }catch (Exception e){
            e.printStackTrace();
            return;
        }

        InputStream iis=null;
        try{
            //输入流：  socket中如何获取输入流
            iis = s.getInputStream();
            byte[] bt = new byte[1024];
            int length=0;
            while ((length=iis.read(bt,0,bt.length))!=-1){
                String str = new String(bt,0,length,"utf-8");
                System.out.println(str);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                iis.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
