package zy.Matrix.V2;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadForRow {
    public static void multiply(double[][] m1, double[][]m2, double[][]r){
        List<Thread> threads = new ArrayList<Thread>();
        int rows1 = m1.length;
        int cols1 = m1[0].length;
        int rows2 = m2.length;
        int cols2 = m2[0].length;

        for(int i = 0;i <rows1; i ++){
            //创建任务，绑定线程，指定元素计算的位置  i j
            OneRowTask oet = new OneRowTask(r, m1, m2, i);
            Thread t = new Thread(oet);
            t.start();
            threads.add(t); //加入集合
            //进行线程运行优先权的控制  join()
            if(threads.size() % 10 == 0){
                waitForThread(threads);
            }
        }
    }

    private static void waitForThread(List<Thread> threads) {
        for(Thread t:threads){
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        threads.clear();
    }
}
