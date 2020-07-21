package ca.jrvs.practice.codingChallenge;

/**
 * https://www.notion.so/Remove-Element-625bb649955f4cbaa6362b6712a0309c
 */
public class RemoveElement {

  /**
   * Big-O: O(n)
   * Space complexity: O(1)
   */
  public static int removeElement(int[] nums, int val) {
    int removed = 0;
    int i = 0;
    int j = nums.length - 1;
    int temp;
    while(i < j) {
      if (nums[i] == val) {
        nums[i] = nums[j];
        j--;
        removed++;
      } else {
        i++;
      }
    }
    return nums.length - removed;
  }
}
