import java.util.HashSet;
import java.util.Set;

public class Solution {
    public int[] twoSum(int[] nums,int target){
        //设置返回值
        int[] index=new int[2];
        //利用set数组不能存放重复数据的原理
        Set set =new HashSet();
        for(int i=0;i<nums.length;i++){
            //如果说目标值减去当前数能添加说明匹配失败
            if(set.add(target-nums[i])){
                set.remove(target-nums[i]);//将判断时候添加内容删除
                set.add(nums[i]);//添加内容
            }else{
                //匹配成功
                index[1]=i;//由于index0>index1,已存在的数下标小
                for (int j = 0; j < nums.length; j++) {
                    if(target==(index[1]+nums[j])){
                        index[0]=j;
                    }
                }
            }
        }
        return index;
    }

    public static void main(String[] args) {
        int[] nums={2,7,11,15};
        int target=9;
        Solution solution = new Solution();
        int[] result = solution.twoSum(nums, target);
        System.out.println(result[0]+""+result[1]);
    }
}
