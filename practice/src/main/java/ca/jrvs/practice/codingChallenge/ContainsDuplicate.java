package ca.jrvs.practice.codingChallenge;

import java.util.HashSet;

/**
 * https://www.notion.so/Contains-Duplicate-6c35919ef8d4403a92e29edb0ea3d0b9
 */
public class ContainsDuplicate {

  /**
   * Big-O: O(n)
   * Space complexity: O(n)
   */
  public boolean containsDuplicateSet(int[] nums) {
    HashSet<Integer> intSet = new HashSet<>();

    for(int num : nums) {
      if (!intSet.contains(num)) {
        intSet.add(num);
      } else {
        return true;
      }
    }
    return false;
  }
}
