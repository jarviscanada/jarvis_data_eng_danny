package ca.jrvs.practice.codingChallenge;

import java.util.Arrays;

/**
 * https://www.notion.so/Valid-Anagram-41dc741e21ec42dabce3c903ec60cd2c
 */
public class ValidAnagram {
  public boolean anagramSort(String a, String b) {
    char[] aChar = a.toCharArray();
    Arrays.sort(aChar);
    String newA = new String(aChar);
    char[] bChar = b.toCharArray();
    Arrays.sort(bChar);
    String newB = new String(bChar);
    return newA.equals(newB);
  }

  public boolean anagramMap(String a, String b) {
    if (a.length() != b.length()) {
      return false;
    }

    int[] alphabetMap = new int[26];
    for (int i = 0; i < a.length(); i++) {
      alphabetMap[a.charAt(i) - 'a']++;
      alphabetMap[b.charAt(i) - 'a']--;
    }

    for (int j = 0; j < alphabetMap.length; j++) {
      if (alphabetMap[j] != 0) {
        return false;
      }
    }
    return true;
  }
}
