package zy.ATM.V2;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class BankTask implements  Runnable {
    private Socket s;
    private boolean flag = true;
    private DbHelper db = new DbHelper();

    public BankTask(Socket s){
        this.s = s;
    }
    @Override
    public void run() {
        try(
             Socket s = this.s;
             Scanner reader = new Scanner(s.getInputStream());
             PrintWriter pw = new PrintWriter(s.getOutputStream());
             ){
            BankAccount ba = null;
            while (flag){
                if (!reader.hasNext()){
                    System.out.println("客户端"+s.getRemoteSocketAddress()+"掉线了");
                    break;
                }
//              reader.nextLine();//按行读
                String command =  reader.next();//按空格符读取数据
                if ("DEPOSITE".equalsIgnoreCase(command)){
                    int id = reader.nextInt();
                    double
                            money= reader.nextDouble();
                    String sql = " update atm set money = money+? where id = ?";
                    int update = db.update(sql, money, id);
                    sql = "select * from atm where id=?";
                    ba = db.findSingle(sql,BankAccount.class,id);

                }else if("WITHDRAW".equalsIgnoreCase(command)){
                    int id = reader.nextInt();
                    double  money= reader.nextDouble();

                    String sql = "select * from atm where id=?";
                    ba = db.findSingle(sql,BankAccount.class,id);
                    if (ba.getMoney()<money ){
                        pw.println("余额不足");
                        pw.flush();
                        continue;
                    }
                   sql = " update atm set money = money-? where id = ?";
                    db.update(sql,money,id);
                    ba.setMoney(ba.getMoney()-money);
                }else if("BALANCE".equalsIgnoreCase(command)){
                    int id = reader.nextInt();
                    String sql = "select * from atm where id=?";
                    ba = db.findSingle(sql,BankAccount.class,id);
                }else if("QUIT".equalsIgnoreCase(command)){
                    System.out.println("客户端"+s.getRemoteSocketAddress()+"主动要求断开");
                    break;
                }else {
                    //错误的命令
                    pw.println("系统不支持此命令"+command);
                    pw.flush();
                    continue;
                }
                pw.println(ba.getId()+"  "+ba.getMoney());
                pw.flush();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
