package ca.jrvs.practice.codingChallenge;

/**
 * https://www.notion.so/String-to-Integer-atoi-6d9f55c2b17a4bdabb5d61972239afb2
 */
public class StringToInteger {

  public int atoiUsingParse(String str) {
    //trim whitespace
    if (str.length() == 0) {
      return 0;
    }
    str = str.trim();

    //check for preceding sign
    int modifier = 1;
    int i = 0;
    if (str.charAt(i) == '-') {
      modifier = -1;
      i++;
    }

    //parse remaining
    return Integer.parseInt(str.substring(i, str.length() - 1)) * modifier;
  }

  /**
   * Big-O: O(n)
   */
  public int atoiUsingASCII(String str) {
    int ASCII_0 = '0';
    int ASCII_9 = '9';

    if (str.length() == 0) {
      return 0;
    }

    int modifier = 1;
    Long num = new Long(0);
    for (int i = 0; i < str.length(); i++) {
      //skip whitespace and determine sign
      char curr = str.charAt(i);
      if (curr == ' ' || curr == '+') {
        continue;
      } else if (curr == '-') {
        modifier = -1;
      } else if ((int) curr >= ASCII_0 && (int) curr <= ASCII_9) {
        num = num * 10 + curr - '0';
        if (num > Integer.MAX_VALUE) {
          return Integer.MAX_VALUE * modifier;
        }
      } else {
        return num.intValue() * modifier;
      }
    }
    return num.intValue() * modifier;
  }
}
