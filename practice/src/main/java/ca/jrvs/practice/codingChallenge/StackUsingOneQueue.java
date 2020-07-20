package ca.jrvs.practice.codingChallenge;

import java.util.LinkedList;
import java.util.Queue;

/**
 * https://www.notion.so/Implement-Stack-using-Queue-caba8c088fac4bd3b85c3ad640a58b1b
 */
public class StackUsingOneQueue {

  private final Queue<Integer> queue = new LinkedList<>();
  Integer top;

  public StackUsingOneQueue() {
  }

  /**
   * Push element x onto stack.
   */
  public void push(int x) {
    queue.add(x);
    int size = queue.size();
    while (size > 1) {
      queue.add(queue.remove());
      size--;
    }
  }

  /**
   * Removes the element on top of the stack and returns that element.
   */
  public int pop() {
    return queue.remove();
  }
}
