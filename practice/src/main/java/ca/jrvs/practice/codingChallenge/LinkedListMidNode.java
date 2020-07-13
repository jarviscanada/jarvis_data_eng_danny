package ca.jrvs.practice.codingChallenge;

/**
 * https://www.notion.so/Middle-of-the-Linked-List-28f631d081b44fc7bd1ab89a08e19b2c
 */
public class LinkedListMidNode {

  /**
   * Big-O: O(n)
   * Space complexity: O(1)
   * Justification: Single traversal, extra space is constant
   */
  public ListNode middleNode(ListNode head) {
    ListNode first = head;
    ListNode second = head;
    while (first != null && first.next != null) {
      first = first.next.next;
      second = second.next;
    }
    return second;
  }
}