package zy.Internet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Test10_udp_receiver {
    public static void main(String[] args) throws IOException {
        byte[] bs = new byte[1024];
        DatagramPacket dp = new DatagramPacket(bs,bs.length);

        DatagramSocket ds = new DatagramSocket(20000);
        while (true){
            System.out.println("接收操作肯定是阻塞式的...");
            ds.receive(dp);
            System.out.println(new String(bs,0,dp.getLength()));
        }
    }
}
