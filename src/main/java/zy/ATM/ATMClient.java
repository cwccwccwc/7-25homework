package zy.ATM;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ATMClient {
    public static void main(String[] args) {
        String atmHost="localhost";
        int port=12000;

        //从键盘输入
        Scanner keyboard = new Scanner(System.in);
        boolean flag=true;
        String respond=null;

        //创建与服务器的连接
        try(
                Socket s = new Socket(atmHost, port);
                //从socket中取出输入流输出流
                PrintWriter pw = new PrintWriter(s.getOutputStream());
                Scanner sc = new Scanner(s.getInputStream());
                ) {
            System.out.println("ATM机正常启动，连接总行服务器成功");
            do {
                System.out.println("=======中国银行ATM服务欢迎你=========");
                System.out.println("1 .存");
                System.out.println("2 .取");
                System.out.println("3 .查询");
                System.out.println("4 .退出");
                System.out.println("=================================");
                System.out.println("请输入你的操作");

                String cinnand = keyboard.nextLine();
                if ("1".equalsIgnoreCase(cinnand)){
                    pw.println("DEPOSITE 1 100");
                }else if ("2".equalsIgnoreCase(cinnand)){
                    pw.println("WITHDRAW 1 5");
                }else if ("3".equalsIgnoreCase(cinnand)){
                    pw.println("BALANCE 1");
                }else if ("4".equalsIgnoreCase(cinnand)){
                    pw.println("QUIT 1");
                    flag=false;
                }else{
                    System.out.println("无此选项");
                    continue;
                }
                pw.flush();
                //服务器的回应
                respond=sc.nextLine();
                if (respond!=null) {
                    System.out.println("服务器的响应为：" + respond);
                }
            }while (flag);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("客户端无法连接服务器，请与维修人员连续...");
        }
        System.out.println("客户端退出");


    }
}
