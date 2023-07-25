package zy.Matrix.V2;

public class OneRowTask implements Runnable {
    private final double[][] r;
    private final double[][] m1;
    private final double[][] m2;

    private final int i;

    public OneRowTask(double[][] r, double[][] m1, double[][] m2, int i) {
        this.r = r;
        this.m1 = m1;
        this.m2 = m2;
        this.i = i;
    }

    @Override
    public void run() {
        for(int j = 0; j < m2[0].length; j ++){
            r[i][j] = 0;
            for(int k = 0; k < m1[i].length; k++){
                r[i][j] += m1[i][k] * m2[k][j];
            }
        }
    }
}
