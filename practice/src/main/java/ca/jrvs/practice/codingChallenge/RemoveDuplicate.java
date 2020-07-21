package ca.jrvs.practice.codingChallenge;

/**
 * https://www.notion.so/Duplicates-from-Sorted-Array-aa1941bdb5c8495a93f1d2244f873045
 */
public class RemoveDuplicate {

  /**
   * Big-O: O(n)
   * Space complexity: O(1)
   */
  public int removeDuplicates(int[] nums) {
    if (nums.length == 1) return 1;
    int i = 0;
    int j = 1;
    while (j < nums.length) {
      if (nums[i] >= nums[j]) {
        while (j < nums.length - 1 && nums[i] >= nums[j]) {
          j++;
        }
        nums[i+1] = nums[j];
      }
      i++;
      j++;
    }
    return i + 1;
  }
}
