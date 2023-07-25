package zy.ATM.V2;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class ATMClient {
    public static void main(String[] args) {
        String atmhost = "localhost";
        int port = 60000;

        //从键盘输入
        Scanner keyboard =new Scanner(System.in);
        boolean flag = true;
        String response = null;

        try(
             //创建于服务器的联接
             Socket s= new Socket(atmhost,port);
             //从Socket中取出输入输出流
             PrintWriter pw = new PrintWriter(s.getOutputStream());
             Scanner sc = new Scanner(s.getInputStream());)
        {
            System.out.println("ATm启动，连接成功");

            do{
                System.out.println("====中国银行服务欢迎你====");
                System.out.println("1.存100");
                System.out.println("2.取5");
                System.out.println("3.查询");
                System.out.println("4.退出");
                System.out.println("请输入您的操作");

                String command = keyboard.nextLine();
                if ("1".equalsIgnoreCase(command)){
                    pw.println("DEPOSITE 1 100");
                }else if ("2".equalsIgnoreCase(command)){
                    pw.println("WITHDRAW 1 5");
                }else if ("3".equalsIgnoreCase(command)){
                    pw.println("BALANCE 1");
                }else if ("4".equalsIgnoreCase(command)){
                    pw.println("QUIT");
                    break;
                }else {
                    System.out.println("无此选项");
                    continue;
                }
                pw.flush();
                //服务器的响应
                response = sc.nextLine();
                System.out.println("服务器的响应为:"+response);
            }while (flag);
        }catch (Exception ex){
            ex.printStackTrace();
            System.out.println("客户端无法联接服务器，请与维修人员联系");
        }
        System.out.println("客户退出");

    }
}

