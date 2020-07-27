package ca.jrvs.practice.codingChallenge;

import java.util.Stack;

/**
 * https://www.notion.so/Implement-Queue-using-Stacks-1611dd32efc8421abcb59dbfe59ccb6f
 */
public class QueueUsingStacksPart1 {

  private final Stack<Integer> s1 = new Stack<>();
  private final Stack<Integer> s2 = new Stack<>();
  private Integer front;

  public QueueUsingStacksPart1() {
  }

  /**
   * Push element x to the back of queue.
   */
  public void push(int x) {
    while (!s1.isEmpty()) {
      s2.push(s1.pop());
    }
    s1.push(x);
    front = x;
    while (!s2.isEmpty()) {
      s1.push(s2.pop());
    }
  }

  /**
   * Removes the element from in front of queue and returns that element.
   */
  public int pop() {
    int temp = s1.pop();
    front = s1.peek();
    return temp;
  }

  /**
   * Get the front element.
   */
  public int peek() {
    return front;
  }

  /**
   * Returns whether the queue is empty.
   */
  public boolean empty() {
    return s1.isEmpty();
  }
}
