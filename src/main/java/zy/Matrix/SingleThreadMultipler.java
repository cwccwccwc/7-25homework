package zy.Matrix;

public class SingleThreadMultipler {

    public static void multiply(double[][]m1,double[][]m2,double[][]r){
        int row1 = m1.length;
        int columns1 = m1[0].length;
        int row2 = m2.length;
        int columns2 = m2[0].length;

        for (int i = 0; i < row1; i++) {
            for (int j = 0; j < columns2; j++) {
                r[i][j]=0;
                for (int k = 0; k < columns1; k++) {
                    r[i][j]+=m1[i][k]*m2[k][j];
                }
            }
        }
    }
}
