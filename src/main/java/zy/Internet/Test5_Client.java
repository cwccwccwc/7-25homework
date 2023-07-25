package zy.Internet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Test5_Client {
    public static void main(String[] args) {
        Socket s=null;
        try{
            s=new Socket("localhost",10001);
            System.out.println("连接成功"+s);
        }catch (Exception e){
            e.printStackTrace();
            return;
        }


        try( InputStream iis=s.getInputStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ){
            //输入流：  socket中如何获取输入流
            byte[] bt = new byte[1024];
            int length=0;
            while ((length=iis.read(bt,0,bt.length))!=-1){
                baos.write(bt,0,length);
            }
            byte[] result = baos.toByteArray();
            String content = new String(result);
            System.out.println(content);
        }catch (Exception e){
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
