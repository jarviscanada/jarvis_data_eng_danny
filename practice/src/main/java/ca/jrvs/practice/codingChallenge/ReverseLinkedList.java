package ca.jrvs.practice.codingChallenge;

/**
 * https://www.notion.so/Reverse-Linked-List-ec459bbac4c540589b836fb13b094357
 */
public class ReverseLinkedList {

  /**
   * Big-O: O(n)
   * Space complexity: O(1)
   */
  public ListNode reverseListIterative(ListNode head) {
    ListNode prev = null;
    ListNode curr = head;
    while (curr != null) {
      ListNode nextTemp = curr.next;
      curr.next = prev;
      prev = curr;
      curr = nextTemp;
    }
    return prev;
  }

  /**
   * Big-O: O(n)
   * Space complexity: O(1)
   */
  public ListNode reverseList(ListNode head) {
    if (head == null || head.next == null) {
      return head;
    }

    ListNode node = reverseList(head.next);
    head.next.next = head;
    head.next = null;
    return node;
  }
}
