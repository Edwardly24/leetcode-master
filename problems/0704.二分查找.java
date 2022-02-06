class Solution {

    public static void main(String[] args) {
        int[] nums = new int[]{1, 2, 3, 4, 7, 9, 10};
        int target = 8;
        Solution solution = new Solution();
        int rst = solution.search2(nums, target);
        System.out.println(rst);
    }

    public int search(int[] nums, int target) {

        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int middle = left + ((right - left) / 2);
            if (nums[middle] > target) {// 如果target大于中间值，由于数组是升序排序且元素唯一的，所以target只可能在左侧
                // 所以要修改右侧的区间
                right = middle - 1;
            } else if (nums[middle] < target) {
                left = middle + 1;
            } else {
                return middle;
            }
        }

        return -1;
    }

    /**
     * 方法2的区间和方法1的搜索区间不一样，注意二者的区别
     * @param nums
     * @param target
     * @return
     */
    public int search2(int[] nums, int target) {

        int left = 0;
        int right = nums.length;
        while (left < right) {
            int middle = left + (int) Math.ceil((right - left) / 2);
            if (nums[middle] > target) {// 如果target大于中间值，由于数组是升序排序且元素唯一的，所以target只可能在左侧
                // 所以要修改右侧的区间
                right = middle;
            } else if (nums[middle] < target) {
                left = middle + 1;
            } else {
                return middle;
            }
        }

        return -1;
    }

}