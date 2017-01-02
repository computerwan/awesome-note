package dataStructres.sort;

/**
 * Created by Wan on 2016/9/4 0004.
 */
public class BasicSort {
    /**
     * 冒泡排序（大泡泡的先出来）
     * 思路：比较相邻的数，将大的往后放
     * 注：这里为什么n-1，n-i-1，因为比较的时候使用j和j+1比较，防止越界。
     */

    public int[] bubbleSort(int[] arr, int n) {
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    exch(arr, j, j + 1);
                }
            }
        }
        return arr;
    }

    private void exch(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    /**
     * 选择排序(将最小值选出来)
     * 选择数组中最小的，与第一个元素交换，依次进行。
     * 注:这里的范围要到n,因为要比较出所有数中最小的值。同时min保存的是数组的下标，不是具体值。
     */
    public int[] selectionSort(int[] arr, int n) {
        for (int i = 0; i < n; i++) {
            int min = i;
            for (int j = i; j < n; j++) {
                if (arr[j] < arr[min]) {
                    min = j;
                }
            }
            exch(arr, i, min);
        }
        return arr;
    }

    /**
     * 插入排序
     * 后面的值依次与前面值做比较，将小值放到前面来
     * 注：因为这里有j-1，故j>0不能等于0
     */
    public void insertSort(int[] arr, int n) {
        for (int i = 1; i < n; i++) {
            for (int j = i; j > 0; j--) {
                if (arr[j] < arr[j - 1]) {
                    exch(arr, j, j - 1);
                }
            }
        }
    }

    /**
     * 希尔排序(改进插入排序)
     * 插入排序的步长为1，而希尔排序的关键：步长的选择
     * 注；j>h-1类似于插入排序中j>0的范围。目的是保证j-h不越界。同时while语句一定要有等于1。
     */
    public int[] shellSort(int[] arr, int n) {
        int h = 1;
        while (h < n / 3)
            h = 3 * h + 1;//初始化步长
        while (h >= 1) {
            for (int i = 0; i < n; i++) {
                for (int j = i; j > h - 1; j -= h) {
                    if (arr[j] < arr[j - h]) {
                        exch(arr, j, j - h);
                    }
                }
            }
            h = h / 3;
        }
        return arr;
    }

}
