package ca.jrvs.practice.codingChallenge;

/**
 * https://www.notion.so/Fibonacci-Number-Climbing-Stairs-22c532e61bc240af8506a57c5a4f0582
 */
public class Fibonacci {

  /**
   * Big-O: (n^2) Space Complexity: O(n) Justification: Every iteration creates two more recursion
   * trees
   */
  public int fib_recursion(int n) {
    if (n > 1) {
      return fib_recursion(n - 1) + fib_recursion(n - 2);
    } else if (n == 1) {
      return 1;
    }
    return 0;
  }

  /**
   * Big-O: (n) Space Complexity: O(1) Justification: Calculating fibonacci(n) takes n loop
   * iterations, using dp array of size n
   */
  public int fib_dynamic(int n) {

    int[] fibArr = new int[n + 1];
    fibArr[0] = 0;
    fibArr[1] = 1;

    for (int i = 2; i <= n; i++) {
      fibArr[i] = fibArr[i - 1] + fibArr[i - 2];
    }

    return fibArr[n];
  }
}
