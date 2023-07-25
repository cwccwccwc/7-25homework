package zy.ATM;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import zy.ATM.Mapper.ATMMapper;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ATMTask implements Runnable{
    private SqlSessionFactory sqlSessionFactory;
    private ATMMapper mapper;
    private Socket s;
    private boolean flag=true;
    public ATMTask(Socket s, SqlSessionFactory sqlSessionFactory) {
        this.s=s;
        this.sqlSessionFactory=sqlSessionFactory;
    }

    @Override
    public void run() {


        try(
                Socket s=this.s;
                Scanner reader = new Scanner(s.getInputStream());
                PrintWriter pw=new PrintWriter(s.getOutputStream());

        ){
            int id;
            double yue;

            while (flag){
                //因为客户端是 pw.println发过来的是一行字符 所以服务端要按行取
                if (!reader.hasNext()){
                    System.out.println("客户端"+s.getRemoteSocketAddress()+"掉线了");
                    break;
                }
                String command = reader.next();//按空格取
                if ("DEPOSITE".equalsIgnoreCase(command)){
                    SqlSession sqlSession = sqlSessionFactory.openSession();
                    mapper=sqlSession.getMapper(ATMMapper.class);
                    id = reader.nextInt();
                    double money = reader.nextDouble();
                    //TODO: jdbc操作 存
                    money = mapper.select(id)+money;
                    mapper.update(id,money);
                    yue = mapper.select(id);
                    sqlSession.commit();
                    sqlSession.close();
                }else if ("WITHDRAW".equalsIgnoreCase(command)){
                    SqlSession sqlSession = sqlSessionFactory.openSession();
                    mapper=sqlSession.getMapper(ATMMapper.class);
                    id = reader.nextInt();
                    double money = reader.nextDouble();
                    //TODO: jdbc操作 取
                    money = mapper.select(id)-money;
                    mapper.update(id,money);
                    yue = mapper.select(id);
                }else if ("BALANCE".equalsIgnoreCase(command)){
                    SqlSession sqlSession = sqlSessionFactory.openSession();
                    mapper=sqlSession.getMapper(ATMMapper.class);
                    id = reader.nextInt();
                    //TODO: jdbc操作 查
                    yue = mapper.select(id);
                }else if ("QUIT".equalsIgnoreCase(command)){
                    System.out.println("客户端"+s.getRemoteSocketAddress()+"主动要求断开");
                    break;
                    //TODO: jdbc操作
                }else{
                    //错误命令
                    pw.println("系统暂不支持此命令");
                    pw.flush();
                    continue;
                }
                //TODO:返回账户 和 余额
                pw.println(id+" "+yue+"￥");
                pw.flush();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
