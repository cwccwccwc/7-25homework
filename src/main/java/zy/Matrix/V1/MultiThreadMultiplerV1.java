package zy.Matrix.V1;

import java.util.ArrayList;
import java.util.List;

/*
* 矩阵中一个元素对应一个线程的方案
* */
public class MultiThreadMultiplerV1 {

    public static void multiply(double[][] m1,double[][]m2,double[][]r){
        List<Thread> threads = new ArrayList<Thread>();
        int row1=m1.length;
        int columns1=m1[0].length;
        int rows2=m2.length;
        int columns2=m2[0].length;

        for (int i = 0; i < row1; i++) {
            for (int j = 0; j < columns2; j++) {
                //创建线程 绑定线程 指定元素计算的位置
                TaskV1 taskV1 = new TaskV1(r, m1, m2, i, j);
                Thread t = new Thread(taskV1);
                t.start();
                threads.add(t);//加入集合
                //进行线程运行优先权的控制 join()
                if (threads.size()%8==0){
                    waitForThread(threads);
                }
            }
        }
    }

    private static void waitForThread(List<Thread> threads) {
            for (Thread t:threads){
                try {
                    t.join();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            threads.clear();
    }
}
