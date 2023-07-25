package zy.chatBot;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket s = new Socket("127.0.0.1", 12000);
        System.out.println("客户端连接到了"+s.getRemoteSocketAddress());

        //System.in 标准输入流 键盘
        //Scanner字符输入流
        try (
                Scanner keyboard = new Scanner(System.in);
                //从socket中取出的与服务器通讯的流
                Scanner clientReader = new Scanner(s.getInputStream());
                PrintWriter pw = new PrintWriter(s.getOutputStream());
                ){

            do {
                System.out.println("请输入客户端想向服务器说的话");
                String line = keyboard.nextLine();
                //用pw输出流向服务器发数据
                pw.println(line);
                pw.flush();

                if ("bye".equalsIgnoreCase(line)){
                    System.out.println("客户端主动断开与服务器的连接");
                    break;
                }
                //取服务器的响应
                String response = clientReader.nextLine();
                System.out.println("服务器的响应为  "+response);
                if ("bye".equalsIgnoreCase(response)){
                    System.out.println("服务端主动断开与客户端的连接");
                    break;
                }
            }while (true);
            System.out.println("客户端与服务器正常断开,程序结束");
        }catch (Exception ex){
            ex.printStackTrace();
        }
        System.out.println("程序结束");

    }
}
