package dataStructres.sort;

/**
 * Created by Wan on 2016/9/4 0004.
 */

/**
 * 注：line16传入init化是传入arr.length - 1
 * merge时候，传入center，第二个数组的起始值为center+1
 */
public class MergeSort {
    //初始化赋值

    public void init(int[] arr) {
        if (arr.length > 0) {
            int[] tmp = new int[arr.length];
            mergeSort(arr, tmp, 0, arr.length - 1);
        }
    }

    private void mergeSort(int[] arr, int[] tmp, int left, int right) {
        if (left < right) {
            int center = (left + right) / 2;
            mergeSort(arr, tmp, left, center);
            mergeSort(arr, tmp, center + 1, right);
            merge(arr, tmp, left, center, right);
        }
    }

    private void merge(int[] arr, int[] tmp, int left, int i, int right) {
        /**
         *  两个数组分别是x~y,m~n
         */
        int x = left;
        int y = i;
        int m = i + 1;
        int n = right;
        int k = 0;
        while (x <= y && m <= n) {
            if (arr[x] <= arr[m]) {
                tmp[k++] = arr[x++];
            } else {
                tmp[k++] = arr[m++];
            }
        }
        while (x <= y) {
            tmp[k++] = arr[x++];
        }
        while (m <= n) {
            tmp[k++] = arr[m++];
        }
        //将临时数组存放到实际数组的后面，实际数组其实从left开头的。
        for (int j = 0; j < k; j++) {
            arr[left + j] = tmp[j];
        }
    }

    //验证
    public static void main(String[] args) {
        int[] arr = {12, 18, 16, 78, 12, 56, 68, 18, 1};
        MergeSort ms = new MergeSort();
        ms.init(arr);
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }
}
