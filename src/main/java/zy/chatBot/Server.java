package zy.chatBot;

import com.alibaba.fastjson.JSON;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket ss=null;

        ss= new ServerSocket(12000);
        System.out.println(ss.getInetAddress()+"启动了，监听端口号:"+ss.getLocalPort());

        while (true){
            Socket s = ss.accept();
            System.out.println("客户端："+s.getRemoteSocketAddress()+"连接上来了");
            //创建新线程 处理与这个客户端的连接
            TalkTask talkTask = new TalkTask(s);
            Thread t = new Thread(talkTask);
            t.start();
        }
    }
}

class TalkTask implements Runnable{
    private Socket s;

    public TalkTask(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        try (
                //从socket中取出的与服务器通讯的流
                Scanner clientReader = new Scanner(s.getInputStream());
                PrintWriter pw = new PrintWriter(s.getOutputStream());
        ){
            do {
                //先接收客户端传来的话
                String response = clientReader.nextLine();
                System.out.println("客户端"+s.getRemoteSocketAddress()+"对服务端说:"+response);
                if ("bye".equalsIgnoreCase(response)){
                    System.out.println("客户端主动断开与服务端的连接");
                    break;
                }

                String responseByApi = getResponseByApi(response);

                //服务端想客户端回话
//                String line = response + ", you too ";//TODO:接入自动聊天机器人api
                pw.println(responseByApi);
                pw.flush();
                if ("bye".equalsIgnoreCase(responseByApi)){
                    System.out.println("服务端主动断开与客户端的连接");
                    break;
                }
            }while (true);


        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private static String getResponseByApi(String smg) throws IOException {
        // url类
        URL url = new URL("http://api.qingyunke.com/api.php?key=free&appid=0&msg="+smg);
        HttpURLConnection  conn = (HttpURLConnection) url.openConnection();

        try (// 读取响应内容
                InputStream iis = conn.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();){
            byte[] bs = new byte[1024];
            int length;
            while ( (length = iis.read(bs,0,bs.length)) != -1) {
                baos.write(bs,0,length);
            }
            baos.flush();
            iis.close();

            byte[] result = baos.toByteArray();
            String response1 = new String(result);

            // 关闭连接
            conn.disconnect();
            ResultBean resultBean = JSON.parseObject(response1, ResultBean.class);
            return resultBean.getContent();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "";
    }
}
