package zy.Matrix;

import java.util.Random;

public class MatrixUtil {
    //矩阵生成工具
    public static double[][] generate(int rows,int columns){
        double[][] ret = new double[rows][columns];
        Random r = new Random();
        for (int i = 0; i < ret.length; i++) {
            for (int j = 0; j < ret[0].length; j++) {
                ret[i][j]=r.nextDouble()*10;
            }
        }
        return ret;
    }
}
