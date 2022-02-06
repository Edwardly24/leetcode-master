class Solution027 {
    public static void main(String[] args) {
        int[] nums = new int[]{1, 2, 2, 5, 6};
        int target = 2;
        int nonTargetNum = removeElement(nums, target);
        System.out.println("no target num: " + nonTargetNum);

    }

    /**
     * 双指针，快指针用于遍历数组，慢指针用于记录非taret的数量
     * 对于这道题目来说，nums[slowIndex] = nums[fastIndex]这句话非必要
     *
     * @param nums
     * @param target
     * @return
     */
    public static int removeElement(int[] nums, int target) {
        int slowIndex = 0;
        for (int fastIndex = 0; fastIndex < nums.length; fastIndex++) {
            if (target != nums[fastIndex]) {//只有不是target的值，才会把target的值占掉，占掉才会增加slowIndex
                //所以，slowIndex记录的就是 非target的数量

                nums[slowIndex] = nums[fastIndex];
                slowIndex++;
            } else {
                System.out.println("the " + (fastIndex + 1) + "th element is target");
            }
        }
        return slowIndex;
    }

}