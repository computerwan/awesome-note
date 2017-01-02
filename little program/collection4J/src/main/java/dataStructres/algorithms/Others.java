package dataStructres.algorithms;

/**
 * Created by Wan on 2016/9/2 0002.
 */
public class Others {
    /**
     * 位运算（剑10，p325）
     */
    public int NumberOf1(int n) {
        int res = 0;
        while (n != 0) {
            res += n & 1;
            n >>>= 1;
        }
        return res;
    }

    /**
     * 数值的整数次方(剑11)
     * 需要考虑exponent大于0，小于0，等于0的情况
     */
    public double Power(double base, int exponent) {
        double res = 1;
        for (int i = 0; i < Math.abs(exponent); i++) {
            res *= base;
        }
        if (exponent < 0) {
            res = 1 / res;
        }
        return res;
    }

    /**
     * 从1到n中1出现的次数（剑32，p429）
     */
    public int NumberOf1Between1AndN_Solution(int n) {
        if (n < 0) {
            return 0;
        }
        int count = 0;
        for (int i = 1; i <= n; i++) {
            count += get1Num(i);
        }
        return count;
    }

    public int get1Num(int num) {
        int res = 0;
        while (num != 0) {
            if (num % 10 == 1) {
                res++;
            }
            num /= 10;
        }
        return res;
    }

    /**
     * 丑数（剑34）
     * 因为丑数其实是p=2^x*3^y*5^z
     * 每次选出来的新丑数，都是三个数里面比较出来最小的值。
     * t2,t3,t5这里表示的是到目前为止至多有几个当前的值
     */
    public int GetUglyNumber_Solution(int index) {
        if (index < 7)
            return index;
        int[] res = new int[index];
        res[0] = 1;
        int t2 = 0, t3 = 0, t5 = 0;
        for (int i = 1; i < index; ++i) {
            res[i] = Math.min(res[t2] * 2, Math.min(res[t3] * 3, res[t5] * 5));
            if (res[i] == res[t2] * 2)
                t2++;
            if (res[i] == res[t3] * 3)
                t3++;
            if (res[i] == res[t5] * 5)
                t5++;
        }
        return res[index - 1];
    }

}
