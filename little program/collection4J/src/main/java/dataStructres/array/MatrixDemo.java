package dataStructres.array;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wan on 2016/9/2 0002.
 */
public class MatrixDemo {
    /**
     * 顺时针打印数组(剑20，p331)
     */
    List<Integer> res = new ArrayList<Integer>();

    public void spiralOrderPrint(int[][] matrix) {
        int tR = 0;
        int tC = 0;
        int dR = matrix.length - 1;
        int dC = matrix[0].length - 1;
        while (tR <= dR && tC <= dC) {
            printEdge(matrix, tR++, tC++, dR--, dC--);
        }
    }

    private void printEdge(int[][] matrix, int tR, int tC, int dR, int dC) {
        if (tR == dR) {
            //只有一行
            for (int i = tC; i < dC; i++) {
                res.add(matrix[tR][i]);
            }
        } else if (tC == dC) {
            //只有一列
            for (int i = tR; i < dR; i++) {
                res.add(matrix[i][tC]);
            }
        } else {
            //一般情况
            int curC = tC;
            int curR = tR;
            while (curC != dC) {
                res.add(matrix[tR][curC]);
                curC++;
            }
            while (curR != dR) {
                res.add(matrix[curR][dC]);
                curR++;
            }
            while (curC != tC) {
                res.add(matrix[dR][curC]);
                curC--;
            }
            while (curR != tR) {
                res.add(matrix[curR][tC]);
                curR--;
            }
        }
    }

    /**
     * 蛇形矩阵
     */
    static int num = 1;

    public  void spiralMatrix(int n) {
        int[][] res = new int[n][n];
        int tR = 0;
        int tC = 0;
        int dR = n-1;
        int dC = n-1;
        while (tR <= dR && tC <= dC) {
            buildMatrix(res, tR++, tC++, dR--, dC--);
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(res[i][j] + " ");
            }
        }
    }

    private void buildMatrix(int[][] res, int tR, int tC, int dR, int dC) {
        int curR = tR;
        int curC = tC;
        if (tR == dR) {
            res[tR][tC] = num;
            num++;
        } else {
            while (curC != dC) {
                res[tR][curC] = num;
                num++;
                curC++;
            }
            while (curR != dR) {
                res[curR][dC] = num;
                num++;
                curR++;
            }
            while (curC != tC) {
                res[dR][curC] = num;
                num++;
                curC--;
            }
            while (curR != tR) {
                res[curR][tC] = num;
                num++;
                curR--;
            }
        }
    }

    public static void main(String[] args) {
        MatrixDemo matrixDemo = new MatrixDemo();
        matrixDemo.spiralMatrix(4);
    }

}
