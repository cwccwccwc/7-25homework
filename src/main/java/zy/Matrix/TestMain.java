package zy.Matrix;

import zy.Matrix.V1.MultiThreadMultiplerV1;
import zy.Matrix.V2.MultiThreadForRow;
import zy.Matrix.V3.MultiThreadForCore;

import java.util.Date;

public class TestMain {
    public static void main(String[] args) {
        int i = Runtime.getRuntime().availableProcessors();
        System.out.println("可以核"+i);
        //生成矩阵
        double[][] m1 = MatrixUtil.generate(2000, 2000);
        double[][] m2 = MatrixUtil.generate(2000, 2000);

        double[][] r = new double[m1.length][m2[0].length];

        Date start,end;
        start=new Date();
        SingleThreadMultipler.multiply(m1,m2,r);
        end=new Date();
        System.out.printf("串行运行的数据为%d ms \n",end.getTime()-start.getTime());

        start=new Date();
        MultiThreadMultiplerV1.multiply(m1,m2,r);
        end=new Date();
        System.out.printf("一个元素的并行运行的数据为%d ms  %S\n",end.getTime()-start.getTime(),r[0][0]);

        start=new Date();
        MultiThreadForRow.multiply(m1,m2,r);
        end=new Date();
        System.out.printf("一行的并行运行的数据为%d ms  %S\n",end.getTime()-start.getTime(),r[0][0]);

        start=new Date();
        MultiThreadForCore.multiply(m1,m2,r);
        end=new Date();
        System.out.printf("一个核开一个线程的并行运行的数据为%d ms  %S\n",end.getTime()-start.getTime(),r[0][0]);
    }
}
