package dataStructres.array;

import java.util.*;

/**
 * Created by Wan on 2016/8/25 0025.
 */
public class arrayDemo {

    /**
     * 两个指针的问题外面一定有一个判断条件。
     */

    /**
     * 二维数组的查找（剑3）
     * 选取右上角的值，如果比其小则向左移动，如果比其大则向下移动
     */
    public boolean findInTwoArray(int[][] arr, int number) {
        if (arr == null) {
            return false;
        }
        int column = arr[0].length - 1, row = 0;//定位到第一行最后一列
        while (row < arr.length && column >= 0) {
            if (arr[row][column] == number) {
                return true;
            }
            if (arr[row][column] > number) {
                column--;
            } else {
                row++;
            }
        }
        return false;
    }

    /**
     * 旋转数组的最小值（引申：在数组中找到一个局部最小的位置）（剑8，p371）
     * 用二分法找到最小值--> 最终指向的两个相邻的元素，而第二个指针则刚好指向最小值
     * 注：需要考虑数值相等的情况和开始已经有序的情况
     */
    public int getLessIndex(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int left = 0;
        int right = arr.length - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] > arr[right]) {
                left = mid + 1; //防止死循环
            } else if (arr[mid] == arr[right]) {//防止重复
                right = right - 1;
            } else {
                right = mid;
            }
        }
        return arr[left];
    }

    /**
     * 调整数组的顺序使得奇数位于偶数前面(剑14)
     * 使用两个指针，前一个指针指向偶数，后一个指针指向奇数，交换两个指针的值
     * 注：该方法不能保证奇数和偶数原来的相对顺序
     */

    public void exchange(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int even = 0;
        int odd = arr.length - 1;
        while (even < odd) {
            if (arr[even] % 2 == 0) {
                if (arr[odd] % 2 == 1) {
                    swap(arr, even, odd);
                }
                odd--;
            } else {
                even++;
            }
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i] + " ");
        }
    }

    public void swap(int[] arr, int index1, int index2) {
        int tmp = arr[index1];
        arr[index1] = arr[index2];
        arr[index2] = tmp;
    }

    /**
     * 上题如果要保证奇数和偶数的相对位置
     * 使用冒泡排序的思路
     */
    public void reOrderArray(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] % 2 == 0 && array[j + 1] % 2 == 1) {
                    swap(array, j, j + 1);
                }
            }
        }
    }

    /**
     * 在数组中出现次数超过一半的数字（剑29，p343）
     * 因为该数出现的次数比其他总和要多
     * 1、遍历一个数字，如果遍历的下一个数字与当前数字相同则加1，否则减1
     * 2、如果值小于0，则保存下一个数字
     */
    public void printHalfMajor(int[] arr) {
        int cand = 0;
        int times = 0;//出现的次数
        for (int i = 0; i < arr.length; i++) {
            if (times == 0) {
                cand = arr[i];
                times = 1;
            } else if (arr[i] == cand) {
                times++;
            } else {
                times--;
            }

        }
        //检查该数字是否大于一半
        times = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == cand) {
                times++;
            }
        }
        if (times > arr.length / 2) {
            System.out.println(cand);
        } else {
            System.out.println("no such number.");
        }
    }

    /**
     * 连续子数组的最大和（剑31，p367）
     * 1、保留最大子数组的和
     * 2、若之前的和小于0，则舍弃前面的值
     */
    public int maxSum(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int max = Integer.MIN_VALUE;
        int cur = 0;
        for (int i = 0; i < arr.length; i++) {
            cur += arr[i];
            max = Math.max(max, cur);
            cur = cur < 0 ? 0 : cur;
        }
        return max;
    }

    /**
     * 把数组排成最小的数（剑33，p281）
     * 将数字转化为字符串-->通过字符串比大小的方式比较mn和nm的大小
     */
    //TODO 重点掌握
    public class MyComparator implements Comparator<String> {
        public int compare(String a, String b) {
            return (a + b).compareTo(b + a);
        }
    }

    public String lowestString(int[] numbers) {
        if (numbers == null || numbers.length < 0) {
            return "";
        }
        String[] strs = new String[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            strs[i] = String.valueOf(numbers[i]);
        }
        Arrays.sort(strs, new MyComparator());
        String res = "";
        for (int i = 0; i < strs.length; i++) {
            res += strs[i];
        }
        return res;
    }

    /**
     * 数组逆序对问题（剑36）
     *
     */


    /**
     * 数字在排序数组中出现的次数（剑38）
     * 因为是排序的，故通过二分查找找到开始的k和结束的k。
     */
    public int getNumberOfK(int[] arr, int k) {
        int number = 0;
        int length = arr.length;
        int first = GetFirstK(arr, k, 0, length - 1);
        int last = GetLastK(arr, k, 0, length - 1);
        if (first > -1 && last > -1) {
            number = last - first + 1;
        }
        return number;
    }

    private int GetFirstK(int[] arr, int k, int start, int end) {
        if (start > end) {
            return -1;
        }
        int middleIndex = (start + end) / 2;
        int middleData = arr[middleIndex];
        if (middleData == k) {
            if ((middleIndex > 0) && arr[middleIndex - 1] != k || middleIndex == 0) {//考虑当前只在边界的情况
                return middleIndex;
            } else {
                end = middleIndex - 1;
            }
        } else if (middleData > k) {
            end = middleIndex - 1;
        } else {
            start = middleIndex + 1;
        }
        return GetFirstK(arr, k, start, end);
    }

    private int GetLastK(int[] arr, int k, int start, int end) {
        int length = arr.length;
        if (start > end) {
            return -1;
        }
        int middleIndex = (start + end) / 2;
        int middleData = arr[middleIndex];
        if (middleData == k) {
            if (middleIndex < length - 1 && arr[middleIndex + 1] != k || middleIndex == length - 1) {//考虑当前只在边界的情况
                return middleIndex;
            } else {
                start = middleIndex + 1;
            }
        } else if (middleData < k) {
            start = middleIndex + 1;
        } else {
            end = middleIndex - 1;
        }
        return GetLastK(arr, k, start, end);
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 3, 3, 3, 4, 5};
        arrayDemo arrayDemo = new arrayDemo();
        System.out.println(arrayDemo.getNumberOfK(arr, 3));
    }


    /**
     * 其它数均出现k次，数组中只出现过一次的数字（剑40，p329）
     * 可以使用位运算，也可以使用hash表
     * 相类似题目(剑51)
     */
    public void FindNumsAppearOnce(int[] array, int num1[], int num2[]) {
        LinkedHashMap<Integer, Integer> hash = new LinkedHashMap<Integer, Integer>();
        ArrayList<Integer> list = new ArrayList<Integer>();

        for (int i = 0; i < array.length; i++) {
            if (hash.containsKey(array[i])) {
                hash.put(array[i], hash.get(array[i]) + 1);
            } else {
                hash.put(array[i], 1);
            }

        }
        for (int i = 0; i < array.length; i++) {
            if (hash.get(array[i]) == 1) {
                list.add(array[i]);
            }
        }
        num1[0] = list.get(0);
        num2[0] = list.get(1);
    }


    /**
     * 和为s的两个数VS和为S的连续正数序列（剑41，p351，p354）
     * 类似题目：p351，未排序（p355）
     * 思路1：排序的数组，通过两个指针：从前和后向中间压缩
     * 思路2：设两个指针，一次遍历，如果比给定值小则fast指向向前遍历，如果比给定值大，则slow指针向前遍历
     */
    public void printUniquePair(int[] arr, int k) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int left = 0;
        int right = arr.length - 1;
        while (left < right) {
            if (arr[left] + arr[right] < k) {
                left++;
            } else if (arr[left] + arr[right] > k) {
                right--;
            } else {
                if (left == 0 || arr[left - 1] != arr[left]) {
                    System.out.println(arr[left] + " " + arr[right]);
                }
                left++;
                right--;
            }
        }
    }

    //和为连续正整数
    public static ArrayList<ArrayList<Integer>> FindContinuousSequence(int sum) {
        ArrayList<ArrayList<Integer>> results = new ArrayList<ArrayList<Integer>>();
        if (sum < 3) {
            return results;
        }
        int slow = 1;
        int fast = 2;
        int curSum = slow + fast;
        for (int i = 1; i <= sum && slow < fast; i++) {//slow<fast必须存在，否则会出现两者指向同一个
            if (curSum < sum) {
                fast++;
                curSum += fast;
            } else if (curSum > sum) {
                curSum -= slow;
                slow++;
            } else {
                ArrayList<Integer> result = new ArrayList<Integer>();
                for (int j = slow; j <= fast; j++) {
                    result.add(j);
                }
                results.add(result);
                fast++;
                curSum = curSum + fast - slow;
                slow++;
            }
        }
        return results;
    }

    /**
     * 数组中重复的数字（剑51）
     */
    public static boolean duplicate(int numbers[], int length, int[] duplication) {
        if (numbers == null || numbers.length < 0) {
            return false;
        }
        LinkedHashMap<Integer, Integer> hash = new LinkedHashMap<Integer, Integer>();
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < numbers.length; i++) {
            if (hash.containsKey(numbers[i])) {
                list.add(numbers[i]);
            } else {
                hash.put(numbers[i], 1);
            }
        }
        if (list.size() == 0) {
            return false;
        }
        for (int i = 0; i < list.size() - 1; i++) {
            duplication[i] = list.get(i);
        }
        return true;

    }

    /**
     * 构造乘积数组（剑52）
     * 1、将其分为两个部分，第一部分计算前i-1个元素的乘积，第二部分计算i+1往后的乘积
     * -->使用动态规划
     * 2、计算B[i]的值
     */
    public int[] multiply(int[] A) {
        int length = A.length;
        int[] B = new int[length];
        if (length != 0) {
            B[0] = 1;
            //计算下三角形连乘
            for (int i = 1; i < length; i++) {
                B[i] = B[i - 1] * A[i - 1];
            }
            int temp = 1;
            //计算上三角
            for (int j = length - 2; j >= 0; j--) {
                temp *= A[j - 1];
                B[j] *= temp;
            }
        }
        return B;
    }

    /**
     * 滑动窗口的最大值（剑65，p19）
     * 关键使用双端队列来实现，其中qmax中存放arr中的**[下标]**
     * qmax队列中是按照大小顺序排列的。
     * （1）如果qmax为空，或者当前值比队列对尾的值小，则addLast
     * （2）如果当前值比队尾的值大，则弹出对尾的值，如此循环
     * 如果当前的下标等于i-w的时候，则将该值出队列。
     */
    public ArrayList<Integer> maxInWindows(int[] num, int size) {
        ArrayList<Integer> res = new ArrayList<Integer>();
        if (num == null || size < 1 || size > num.length) {
            return res;
        }
        LinkedList<Integer> qmax = new LinkedList<Integer>();

        int index = 0;
        for (int i = 0; i < num.length; i++) {
            while (!qmax.isEmpty() && num[qmax.peekLast()] <= num[i]) { //因为qmax中保存的是下标，故这里比较大小的时候要取出值
                qmax.pollLast();
            }
            qmax.addLast(i);
            if (qmax.peekFirst() == i - size) {
                qmax.pollFirst();
            }
            if (i >= size - 1) {
                res.add(num[qmax.peekFirst()]);
            }
        }
        return res;
    }


}
