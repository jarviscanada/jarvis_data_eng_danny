package ca.jrvs.practice.codingChallenge;

/**
 * https://www.notion.so/Nth-Node-From-End-of-LinkedList-c82032b50d884db3befd1bd8dc59f3bc
 */

public class LinkedListNthNode<E> {

  /**
   * Big-O: O(n)
   * Space complexity: O(1)
   * Justification: Single traversal, constant extra space used
   */
  public ListNode removeNthFromEnd(ListNode head, int n) {
    ListNode dummy = new ListNode(0);
    dummy.next = head;
    int len = 0;
    ListNode first = dummy;
    ListNode second = dummy;

    while(n >= 0 && first != null) {
      first = first.next;
      n--;
    }

    while(first != null) {
      first = first.next;
      second = second.next;
    }
    second.next = second.next.next;
    return dummy.next;
  }
}

class ListNode {
  int val;
  ListNode next;
  ListNode() {}
  ListNode(int val) { this.val = val; }
  ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}