package ca.jrvs.practice.codingChallenge;

/**
 * https://www.notion.so/Count-Primes-557be9fd55a84f17b9d24769140ab27b
 */
public class CountPrimes {

  /**
   * Big-O: O(n^2)
   * Space complexity: O(n)
   */
  public static int countPrimes(int n) {
    if (n <= 0) return 0;
    int count = 0;
    boolean[] notPrime = new boolean[n];
    notPrime[0] = true;
    notPrime[1] = true;

    for(int i = 2; i < Math.sqrt(n); i++) {
      if(!notPrime[i]) {
        for(int j = 2; i * j < n; j++) {
          notPrime[i*j] = true;
        }
      }
    }

    for(int i = 2; i < notPrime.length; i++) {
      if(!notPrime[i]) count++;
    }
    return count;
  }
}
