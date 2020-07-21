package ca.jrvs.practice.codingChallenge;

import java.util.HashMap;
import java.util.Map;

/**
 * https://www.notion.so/Two-Sum-06f58105536d4e9794920126f2359e6e
 */
public class TwoSum {

  /**
   * Big-O: O(n^2) Justification: Worst case, inner loop runs n times
   */
  public int[] twoSumNaive(int[] nums, int target) {
    int sum;

    if (nums.length < 2) {
      return new int[0];
    }

    for (int i = 0; i <= nums.length - 2; i++) {
      for (int j = 1; j <= nums.length - 1; j++) {
        sum = nums[i] + nums[j];
        if (sum == target) {
          return new int[]{i, j};
        }
      }
    }
    return new int[0];
  }

  /**
   * Big-O: O(n) Justification: Worst case, number array is traversed once in its entirety
   */
  public int[] twoSumSorted(int[] nums, int target) {
    int left = 0;
    int right = nums.length - 1;
    int sum;

    if (nums.length < 2) {
      return new int[0];
    }

    while (left < right) {
      sum = nums[left] + nums[right];
      if (sum < target) {
        left++;
      } else if (sum > target) {
        right--;
      } else {
        return new int[]{left, right};
      }
    }
    return new int[0];
  }

  /**
   * Big-O: O(n) Justification: Worst case is a single traversal of the problem set
   */
  public int[] twoSumMap(int[] nums, int target) {
    Map<Integer, Integer> results = new HashMap<>();

    if (nums.length < 2) {
      return new int[0];
    }

    int diff;
    results.put(nums[0], 0);

    for (int i = 1; i < nums.length; i++) {
      diff = target - nums[i];
      if (results.containsKey(diff)) {
        return new int[]{results.get(diff), i};
      } else {
        results.put(nums[i], i);
      }
    }
    return new int[0];
  }
}
