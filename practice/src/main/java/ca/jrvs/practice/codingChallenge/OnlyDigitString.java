package ca.jrvs.practice.codingChallenge;

/**
 * https://www.notion.so/Check-if-a-String-contains-only-digits-5a0a757b38654f0aae321bd82e2e3820
 */
public class OnlyDigitString {

  public boolean onlyDigitsASCII(String str) {
    int ASCII_0 = '0';
    int ASCII_9 = '9';

    for (int i = 0; i < str.length(); i++) {
      int curr = str.charAt(i);
      if (curr < ASCII_0 || curr > ASCII_9) {
        return false;
      }
    }

    return true;
  }

  public boolean onlyDigitsAPI(String str) {
    try {
      Integer.valueOf(str);
    } catch (NumberFormatException ex) {
      return false;
    }
    return true;
  }

  public boolean onlyDigitsRegex(String str) {
    return str.matches("\\d+");
  }
}
