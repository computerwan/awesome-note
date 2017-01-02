package dataStructres.algorithms;

/**
 * Created by Wan on 2016/8/28 0028.
 */
public class Search {
    /**
     * 二分搜索
     */
    int BinSearch(int target, int nums[]) {
        int low = 0, high = nums.length - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (nums[mid] == target)
                return mid;
            if (nums[mid] < target)
                low = mid + 1;
            else
                high = mid - 1;
        }
        return -1;
    }

    /**
     * 二分搜索方法2（high的长度和mid的赋值）
     */
    int BinSearch2(int target, int nums[]) {
        int low = 0, high = nums.length;
        while (low < high) {
            int mid = (low + high) / 2;
            if (nums[mid] == target)
                return mid;
            if (nums[mid] < target)
                low = mid + 1;
            else
                high = mid;
        }
        return -1;
    }
}
