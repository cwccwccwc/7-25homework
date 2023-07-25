package zy.ATM;



import Utils.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSessionFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ATMServer {
    public static void main(String[] args) throws IOException {
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
        int processors = Runtime.getRuntime().availableProcessors();
        ServerSocket ss=null;

        ss= new ServerSocket(12000);
        System.out.println(ss.getInetAddress()+"启动了，中国银行服务器已启动，监听端口号:"+ss.getLocalPort());

        while (true){
            Socket s = ss.accept();
            System.out.println("客户端："+s.getRemoteSocketAddress()+"连接上来了");
            //创建新线程 处理与这个客户端的连接
            ATMTask atmTask = new ATMTask(s,sqlSessionFactory);
            Thread t = new Thread(atmTask);
            t.start();
        }
    }

}
