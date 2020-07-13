package ca.jrvs.practice.codingChallenge;

import java.util.LinkedList;
import java.util.Queue;

/**
 * https://www.notion.so/Implement-Stack-using-Queue-caba8c088fac4bd3b85c3ad640a58b1b
 */
public class StackUsingTwoQueues {

  private Queue<Integer> q1 = new LinkedList<>();
  private Queue<Integer> q2 = new LinkedList<>();
  Integer top;

  public StackUsingTwoQueues() {}

  /** Push element x onto stack. */
  public void push(int x) {
    q1.add(x);
    top = x;
  }

  /** Removes the element on top of the stack and returns that element. */
  public int pop() {
    while(q1.size() > 1) {
      top = q1.remove();
      q2.add(top);
    }
    top = q1.remove();
    Queue<Integer> temp = q1;
    q1 = q2;
    q2 = temp;
    return top;
  }

  /** Get the top element. */
  public int top() {
    return top;
  }

  /** Returns whether the stack is empty. */
  public boolean empty() {
    return q1.isEmpty() && q2.isEmpty();
  }
}
