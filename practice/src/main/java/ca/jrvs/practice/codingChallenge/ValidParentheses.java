package ca.jrvs.practice.codingChallenge;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * https://www.notion.so/Valid-Parentheses-fa9fda0a90fb4d1fb5fe65cc78a3b657
 */
public class ValidParentheses {

  Map<Character, Character> charMap = new HashMap<>();
  Stack<Character> stack = new Stack<>();

  /**
   * Big-O: O(n)
   */
  public ValidParentheses() {
    charMap.put(')', '(');
    charMap.put('}', '{');
    charMap.put(']', '[');
  }

  public boolean isValid(String s) {
    for (int i = 0; i < s.length(); i++) {
      char curr = s.charAt(i);
      if (charMap.containsKey(curr) && !stack.isEmpty()) {
        if (charMap.get(curr) == stack.peek()) {
          stack.pop();
        } else {
          return false;
        }
      } else {
        stack.push(curr);
      }
    }
    return stack.isEmpty();
  }
}
