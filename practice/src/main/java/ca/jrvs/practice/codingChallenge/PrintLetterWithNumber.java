package ca.jrvs.practice.codingChallenge;

/**
 * https://www.notion.so/Print-letter-with-number-3202f4161e6b4ff8b680e690ad8c466f
 */
public class PrintLetterWithNumber {

  public static String print(String str) {
    StringBuilder sb = new StringBuilder();
    char curr;
    int num;
    for(int i = 0; i < str.length(); i++) {
      curr = str.charAt(i);
      num = Character.isUpperCase(curr) ? curr - 'A' + 27 : curr - 'a' + 1;
      sb.append(curr);
      sb.append(num);
    }

    return sb.toString();
  }
}
