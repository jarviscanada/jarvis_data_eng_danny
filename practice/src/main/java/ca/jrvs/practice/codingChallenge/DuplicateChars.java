package ca.jrvs.practice.codingChallenge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https://www.notion.so/Duplicate-Characters-7f9231bde953403c8900fa0de680e827
 */
public class DuplicateChars {

  /**
   * Big-O: O(n)
   * Space complexity: O(n)
   */
  public static List<Character> duplicates(String str) {
    HashMap<Character, Integer> charMap = new HashMap<>();

    for(int i = 0; i < str.length(); i++) {
      char c = str.charAt(i);
      if (!charMap.containsKey(c)) charMap.put(c, 1);
      else charMap.replace(c, charMap.get(c)+1);
    }
    List<Character> charList = new ArrayList<>();
    for(Character character : charMap.keySet()) {
      if (charMap.get(character) > 1) charList.add(character);
    }

    return charList;
  }
}
