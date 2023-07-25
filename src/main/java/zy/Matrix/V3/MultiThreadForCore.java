package zy.Matrix.V3;

import java.util.ArrayList;
import java.util.List;

/*
* 根据 核 的数量开线程 （最优解） 线程数量最好为核的数量 如大于 核数量的两倍
* 则会产生更多的 cpu切换线程时释放垃圾等操作所产生的时间 从而浪费性能
* */
public class MultiThreadForCore {

    public static void multiply(double[][]m1,double[][]m2,double[][]r){
        List<Thread> threads = new ArrayList<>();
        int row1=m1.length;
        int columns1=m1[0].length;
        int rows2=m2.length;
        int columns2=m2[0].length;

        //为每个线程 =》计算 行的起始位置和终点位置
        int numThreads = Runtime.getRuntime().availableProcessors();
        int startIndex,endIndex,step;
        step=row1/numThreads;
        startIndex=0;
        endIndex=step;

        for (int i = 0; i < numThreads; i++) {
            CoreTask coreTask = new CoreTask(r, m1, m2, startIndex, endIndex);
            Thread t = new Thread(coreTask);
            t.start();

            threads.add(t);
            System.out.println("第"+(i+1)+"个线程运行的位置："+startIndex+"---"+endIndex);
            //重新计算第i的起始位置和终止位置
            startIndex=endIndex;
            //如果是最后一个线程，要将最后所有的行放到此线程中运行
            endIndex=    i==numThreads-2?row1:endIndex+step;
        }

        for (Thread t:threads){
            try {
                t.join();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
