package ca.jrvs.practice.codingChallenge;

/**
 * https://www.notion.so/Fibonacci-Number-Climbing-Stairs-22c532e61bc240af8506a57c5a4f0582
 */
public class ClimbingStairs {

  /**
   * Big-O: O(n^2)
   * Space Complexity: O(n)
   * Justification: Every iteration creates two more recursion trees, tree can
   * go up to n levels
   */
  public int stairsRecursion(int n) {
    if (n >= 2) {
      return stairsRecursion(n-1) + stairsRecursion(n-2);
    }
    return 1;
  }

  /**
   * Big-O: O(n)
   * Space Complexity: O(n)
   * Justification: Always takes n loop iterations to calculate possible
   * steps up to n, additional array of size n is used
   */
  public int stairsDynamic(int n) {
    int[] steps = new int[n+1];
    steps[0] = 1;
    steps[1] = 1;

    for(int i = 2; i <= n; i++) {
      steps[i] = steps[i-1] + steps[i-2];
    }
    return steps[n];
  }
}
