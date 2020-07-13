package ca.jrvs.practice.codingChallenge;

/**
 * https://www.notion.so/Rotate-String-95d242727b0e4054af9ee36afa6e49f7
 */
public class RotateString {

  /**
   * Big-O: O(n)
   * Justification: Java String contains runs at O(n)
   */
  public boolean checkString(String a, String b) {
    return a.length() == b.length() && (a+a).contains(b);
  }
}
