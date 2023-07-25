package zy.Matrix.V3;

public class CoreTask implements Runnable{
    private final double[][] r;
    private final double[][] m1;
    private final double[][] m2;

    private final int startIndex;
    private final int endIndex;

    public CoreTask(double[][] r, double[][] m1, double[][] m2, int startIndex, int endIndex) {
        this.r = r;
        this.m1 = m1;
        this.m2 = m2;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }


    @Override
    public void run() {
        for (int i=startIndex;i<endIndex;i++){
            for (int j=0;j<m2[0].length;j++){
                r[i][j]=0;
                for (int k=0;k<m1[i].length;k++){
                    r[i][j]+=m1[i][k]*m2[k][j];
                }
            }
        }
    }
}
