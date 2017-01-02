package dataStructres.sort;

/**
 * Created by Wan on 2016/9/4 0004.
 */
public class QuickSort {
    /**
     * 随机选一个数，小于等于的数放在左边，大于的数放在右边，递归的对两边的数再进行排序。
     * 步骤：
     * 1.划分值放在数组的最后一个位置。
     * 2.设计一个小于等于区间，初始时候长度为0
     * 3.如果与最后一个数比较，小于最后一个数，则放在区间中
     * 4.最后将划分值放在区间后面
     */

    public int getMiddle(int[] list, int low, int high) {
        int tmp = list[low];
        while (low < high) {
            while (low < high && list[high] >= tmp) {
                high--;
            }
            list[low] = list[high];//low的值备份在tmp中
            while (low < high && list[low] <= tmp) {
                low++;
            }
            list[high] = list[low];//high的值备份在low中
        }
        list[low] = tmp;//将最后一个值放到中间
        return low;
    }

    public void quick(int[] str) {
        if (str.length > 0) {
            quickSort(str, 0, str.length - 1);
        }
    }

    private void quickSort(int[] str, int low, int high) {
        if (low < high) {
            int middle = getMiddle(str, low, high);
            quickSort(str, low, middle - 1);
            quickSort(str, middle + 1, high);
        }
    }

    //测试
    public static void main(String[] args) {
        int[] list = {34, 3, 53, 2, 23, 7, 7, 10};
        QuickSort quickSort = new QuickSort();
        quickSort.quick(list);
        for (int i = 0; i < list.length; i++) {
            System.out.print(list[i] + " ");
        }
    }
}
