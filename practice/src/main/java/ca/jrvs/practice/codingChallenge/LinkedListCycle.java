package ca.jrvs.practice.codingChallenge;

/**
 *
 */
public class LinkedListCycle {

  /**
   * Big-O: O(n)
   * Space complexity: O(1)
   * Justification: 
   */
  public boolean hasCycle(ListNode head) {
    if (head == null || head.next == null) {
      return false;
    }

    ListNode first = head.next;
    ListNode second = head;

    while (first != second) {
      if (first == null || first.next == null) {
        return false;
      }
      first = first.next.next;
      second = second.next;
    }
    return true;
  }
}
