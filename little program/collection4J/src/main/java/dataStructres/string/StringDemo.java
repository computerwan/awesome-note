package dataStructres.string;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

/**
 * Created by Wan on 2016/8/25 0025.
 */


public class StringDemo {
    /**
     * 替换空格（剑4，p260）
     * 1、先遍历一遍字符串，统计出字符串的空格的总数
     * 2、从后面开始复制和替换
     */
    public void replace(char[] chas) {
        if (chas == null || chas.length < 0) {
            return;
        }
        int num = 0;
        int len = 0;
        for (len = 0; len < chas.length; len++) {
            if (chas[len] == ' ') {
                num++;
            }
        }
        int j = len + num * 2 - 1;//需要减一，因为len++后才判断
        for (int i = len - 1; i > -1; i--) {
            if (chas[i] != ' ') {
                chas[j--] = chas[i];
            } else {
                chas[j--] = '0';
                chas[j--] = '2';
                chas[j--] = '%';
            }
        }
    }


    /**
     * 字符串排序问题（剑28）
     * 遍历的思想。
     * 1、把字符串分为两部分，第一部分是字符串的第一个字符，第二部分是除第一部分以为的字符
     * 2、拿第一个字符和它后面的逐个做交换
     */
    public ArrayList<String> Permutation(String str) {
        ArrayList<String> res = new ArrayList<String>();

        if (str != null && str.length() > 0) {
            PermutationHelper(str.toCharArray(), 0, res);
        }

        return res;
    }

    private static void PermutationHelper(char[] cs, int i, ArrayList<String> list) {
        if (i == cs.length - 1) { //解空间的一个叶节点
            list.add(String.valueOf(cs)); //找到一个解
        } else {
            for (int j = i; j < cs.length; ++j) {
                if (j == i || cs[j] != cs[i]) {//原始顺序+不重复
                    swap(cs, i, j);
                    PermutationHelper(cs, i + 1, list);
                    swap(cs, i, j); //复位
                }
            }
        }
    }

    public static void swap(char[] cs, int i, int j) {
        char temp = cs[i];
        cs[i] = cs[j];
        cs[j] = temp;
    }

    /**
     * 第一次出现不重复的子字符（剑35）
     * 使用hashMap,map维护的是某个字符串出现的次数。
     */
    public Character firstNotRepeating(String str) {
        if (str == null) {
            return null;
        }
        char[] strChar = str.toCharArray();
        LinkedHashMap<Character, Integer> hash = new LinkedHashMap<Character, Integer>();
        for (char item : strChar) {
            if (hash.containsKey(item)) {
                hash.put(item, hash.get(item) + 1);
            } else {
                hash.put(item, 1);
            }
        }
        for (char key : hash.keySet()) {
            if (hash.get(key) == 1) {
                return key;
            }
        }
        return null;
    }

    /**
     * 翻转单词&左旋转字符串(剑42，p263)
     */
    public String rotateWord(String chas) {
        if (chas == null || chas.trim().equals("")) {
            return chas;
        }
        StringBuilder result = new StringBuilder();
        String tmp = reverse(chas, 0, chas.length() - 1);
        String[] tmpArr = tmp.split(" ");
        for (int i = 0; i < tmpArr.length; i++) {
            result.append(reverse(tmpArr[i], 0, tmpArr[i].length() - 1) + " ");
        }
        return result.toString().trim();

    }

    //字符串反转可以用stringBuilder里面的reverse方法。
    //这里注意char和String的转换：toCharArray和valueOf
    public String reverse(String chas, int start, int end) {
        char[] chars = chas.toCharArray();
        char tmp;
        while (start < end) {
            tmp = chars[start];
            chars[start] = chars[end];
            chars[end] = tmp;
            start++;
            end--;
        }
        return String.valueOf(chars);
    }


    //附录：字符串排序--字典生成算法
    public ArrayList<String> Permutation2(String str) {
        ArrayList<String> res = new ArrayList<String>();

        if (str != null && str.length() > 0) {
            char[] seq = str.toCharArray();
            Arrays.sort(seq); //排列
            res.add(String.valueOf(seq)); //先输出一个解

            int len = seq.length;
            while (true) {
                int p = len - 1, q;
                //从后向前找一个seq[p - 1] < seq[p]
                while (p >= 1 && seq[p - 1] >= seq[p])
                    --p;
                if (p == 0)
                    break; //已经是“最小”的排列，退出
                //从p向后找最后一个比seq[p]大的数
                q = p;
                --p;
                while (q < len && seq[q] > seq[p])
                    q++;
                --q;
                //交换这两个位置上的值
                swap(seq, q, p);
                //将p之后的序列倒序排列
                reverse(seq, p + 1);
                res.add(String.valueOf(seq));
            }
        }

        return res;
    }

    public static void reverse(char[] seq, int start) {
        int len;
        if (seq == null || (len = seq.length) <= start)
            return;
        for (int i = 0; i < ((len - start) >> 1); i++) {
            int p = start + i, q = len - 1 - i;
            if (p != q)
                swap(seq, p, q);
        }
    }

    /**
     * 左旋转字符串（剑42的第二题）
     */
    public String LeftRotateString(String str, int n) {
        int len = str.length();
        if (len == 0) {
            return "";
        }
        n = n % len;
        str += str;
        return str.substring(n, n + len);

    }

    /**
     * 将字符串转化为数字(剑49，p248)
     */
    public int StrToInt(String str) {
        if (str.equals("") || str.length() == 0) {
            return 0;
        }
        int fuhao = 0;
        char[] a = str.toCharArray();
        if (a[0] == '-') {
            fuhao = 1;
        }
        int sum = 0;
        for (int i = fuhao; i < a.length; i++) {
            if (a[i] == '+') {
                continue;
            }
            if (a[i] < 48 || a[i] > 57) {
                return 0;
            }
            sum = sum * 10 + a[i] - 48;
        }
        return fuhao == 0 ? sum : sum * (-1);
    }
}
