package ca.jrvs.practice.codingChallenge;

import java.util.HashSet;

/**
 * https://www.notion.so/Missing-Number-73d15ed60bcf40569165faaad0357c62
 */
public class MissingNumber {
  public int missingNumberSum(int[] nums) {
    int len = nums.length;
    int sum = len*(len+1)/2;
    for(int i=0; i<len; i++)
      sum -= nums[i];
    return sum;
  }

  public int missingNumberHashSet(int[] nums) {
    HashSet<Integer> set = new HashSet<>();

    for (int num : nums) {
      set.add(num);
    }
    int max = nums.length + 1;
    for(int i = 0; i < max; i++) {
      if(!set.contains(i)){
        return i;
      }
    }
    return 0;
  }

  public int missingNumberGauss(int[] nums) {
    int expected = nums.length * (nums.length+1) / 2;
    int actual = 0;
    for(int num : nums) {
      actual += num;
    }
    return expected - actual;
  }
}
