package dataStructres.algorithms;

/**
 * Created by Wan on 2016/9/2 0002.
 */

/**
 * 五大常用算法之一：回溯法
 */
public class Backfitting {
    /**
     * 矩阵中的路径（剑66）
     */
    public boolean hasPath(char[] matrix, int rows, int cols, char[] str) {
        int flag[] = new int[matrix.length];//定义一个和字符矩阵大小一样的boolean值矩阵，来判断是否进入某个格子。
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (helper(matrix, rows, cols, i, j, str, 0, flag)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean helper(char[] matrix, int rows, int cols, int i, int j, char[] str, int k, int[] flag) {
        int index = i * cols + j;
        if (i < 0 || i >= rows || j < 0 || j >= cols || matrix[index] != str[k] || flag[index] == 1)
            return false;
        if (k == str.length - 1)
            return true;
        flag[index] = 1;
        //分别向四个方向递归
        if (helper(matrix, rows, cols, i - 1, j, str, k + 1, flag) || helper(matrix, rows, cols, i + 1, j, str, k + 1, flag)
                || helper(matrix, rows, cols, i, j - 1, str, k + 1, flag)
                || helper(matrix, rows, cols, i, j + 1, str, k + 1, flag)) {
            return true;
        }
        flag[index] = 0;//如果四个方向均不满足，则出栈
        return false;
    }

    /**
     * 机器人运动范围（剑67）
     */
    public int movingCount(int threshold, int rows, int cols) {
        int flag[][] = new int[rows][cols]; //记录是否已经走过
        return helperInMoving(0, 0, rows, cols, flag, threshold);
    }

    private int helperInMoving(int i, int j, int rows, int cols, int[][] flag, int threshold) {
        if (i < 0 || j >= rows || j < 0 || j >= cols || numSum(i) + numSum(j) > threshold || flag[i][j] == 1) {
            return 0;
        }
        flag[i][j] = 1;
        return helperInMoving(i - 1, j, rows, cols, flag, threshold) + helperInMoving(i + 1, j, rows, cols, flag, threshold)
                + helperInMoving(i, j - 1, rows, cols, flag, threshold) + helperInMoving(i, j + 1, rows, cols, flag, threshold) + 1;
    }

    private int numSum(int i) {
        int sum = 0;
        while (i > 0) {
            sum += i % 10;
            i = i / 10;
        }
        return sum;
    }
}
