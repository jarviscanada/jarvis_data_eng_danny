package ca.jrvs.practice.codingChallenge;

/**
 * https://www.notion.so/Swap-two-numbers-cdacf22ec9aa41548f901a140449ba46
 */
public class SwapNumber {

  public static int[] swapArithmetic(int[] arr) {
    arr[1] = arr[0] + arr[1];
    arr[0] = arr[1] - arr[0];
    arr[1] = arr[1] - arr[0];
    return arr;
  }

  public static int[] swapBit(int[] arr) {
    arr[0] = arr[0] ^ arr[1];
    arr[1] = arr[1] ^ arr[0];
    arr[0] = arr[0] ^ arr[1];
    return arr;
  }
}
