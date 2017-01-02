package dataStructres.algorithms;

/**
 * Created by Wan on 2016/8/29 0029.
 */
public class RecursiveAndLoop {
    /**
     * 斐波那契数列（剑9,p181）
     */
    public static int Fibonacci(int n) {
        if (n < 0) {
            return 0;
        }
        if (n == 1 || n == 2) {
            return 1;
        }
        int res = 1;
        int pre = 1;
        for (int i = 3; i <= n; i++) {
            int tmp = res;
            res = res + pre;
            pre = tmp;
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println(Fibonacci(6));
    }

    /**
     * 跳台阶
     */
    public int JumpFloor(int target) {
        if (target < 1) {
            return 0;
        }
        if (target == 1 || target == 2) {
            return target;
        }
        int res = 2;
        int pre = 1;
        for (int i = 3; i <= target; i++) {
            int temp = res;
            res = res + pre;
            pre = temp;
        }
        return res;
    }


    /**
     * 变态跳台阶
     */
    public int JumpFloorII(int target) {
        if (target <= 0)
            return 0;
        return 1 << (target - 1);
    }


    /**
     *
     * 矩形覆盖
     *
     * 我们可以用2*1的小矩形横着或者竖着去覆盖更大的矩形。请问用n个2*1的小矩形无重叠地覆盖一个2*n的大矩形，总共有多少种方法？
     * 结果同跳台阶问题。
     * f(n)可以理解为f(n-1)的矩阵加一个竖者放，f(n-2)的矩阵加两个横着放。
     */

}
