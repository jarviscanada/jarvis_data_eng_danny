package ca.jrvs.practice.codingChallenge;

import java.util.Arrays;
import java.util.HashSet;

/**
 * https://www.notion.so/Find-the-Duplicate-Number-9c3bbe6ee4314cae9066de516ccbd4d2
 */
public class DuplicateNumber {

  /**
   * Big-O: O(nlogn)
   * Space complexity: O(1)
   * Justification: Arrays.sort() uses Dual-Pivot Quicksort, which has O(nlogn) complexity
   * and sorts in-place (hence O(1)).
   */
  public static int duplicateWithSort(int[] nums) {
    Arrays.sort(nums);
    for(int i = 1; i < nums.length; i++) {
      if (nums[i] == nums[i-1]) return nums[i];
    }
    return -1;
  }

  /**
   * Big-O: O(n)
   * Space complexity: O(n)
   * Justification: One traversal of the problem set, along with extra space of
   * O(n) in the HashSet.
   */
  public static int duplicateWithSet(int[] nums) {
    HashSet<Integer> intSet = new HashSet<>();
    for(int num : nums) {
      if (intSet.contains(num)) return num;
      else intSet.add(num);
    }
    return -1;
  }
}
