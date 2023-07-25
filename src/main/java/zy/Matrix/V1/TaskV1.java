package zy.Matrix.V1;
/*
* 一个元素的结果对应一个线程
* */
public class TaskV1 implements Runnable{
    private final double[][] r;
    private final double[][] m1;
    private final double[][] m2;

    private final int i;
    private final int j;

    public TaskV1(double[][] r, double[][] m1, double[][] m2, int i, int j) {
        this.r = r;
        this.m1 = m1;
        this.m2 = m2;
        this.i = i;
        this.j = j;
    }

    @Override
    public void run() {
        r[i][j]=0;
        for (int k = 0; k < m1[i].length;k++) {
            r[i][j]+=m1[i][k]*m2[k][j];
        }
    }
}
