package ca.jrvs.practice.codingChallenge;

import java.util.Stack;

/**
 * https://www.notion.so/Implement-Queue-using-Stacks-1611dd32efc8421abcb59dbfe59ccb6f
 */
public class QueueUsingStacksPart2 {

  private final Stack<Integer> s1 = new Stack<>();
  private final Stack<Integer> s2 = new Stack<>();
  private Integer front;

  public QueueUsingStacksPart2() {
  }

  public void push(int x) {
    if (s1.empty()) {
      front = x;
    }
    s1.push(x);
  }

  public int pop() {
    if (s2.isEmpty()) {
      while (!s1.isEmpty()) {
        s2.push(s1.pop());
      }
    }
    return s2.pop();
  }

  public int peek() {
    if (!s2.isEmpty()) {
      return s2.peek();
    }
    return front;
  }

  public boolean empty() {
    return s1.isEmpty() && s2.isEmpty();
  }
}
