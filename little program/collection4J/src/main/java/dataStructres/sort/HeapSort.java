package dataStructres.sort;

/**
 * Created by Wan on 2016/9/4 0004.
 */
public class HeapSort {
    /**
     * 二叉堆：
     * 1. 两个基本操作，删除最大元素和插入元素
     * 2. 用数组保存，k的父节点为[k/2],子节点为[2k]和[2k+1]
     * 3. 当插入元素时，从由下至上恢复堆顺序（元素加到数组末尾，并上浮swim）
     * 4. 当删除最大元素时，由上到下恢复堆顺序（数组最后一个元素放到顶端，并下沉sink)
     */

    private void swim(int[] arr, int k) {
        while (k > 1 && less(k / 2, k)) {
            exch(arr, k / 2, k);
            k = k / 2;
        }
    }

    private void sink(int[] arr, int k) {
        int N = arr.length;
        while (2 * k < N) {
            int j = 2 * k;//j为k的子节点
            if (j < N && less(j, j + 1)) {
                j++;
            }
            if (!less(k, j))
                break;
            exch(arr, k, j);
            k = j;//下移

        }
    }


    public void exch(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public boolean less(int i, int j) {
        if (i < j) {
            return true;
        } else {
            return false;
        }
    }

}
