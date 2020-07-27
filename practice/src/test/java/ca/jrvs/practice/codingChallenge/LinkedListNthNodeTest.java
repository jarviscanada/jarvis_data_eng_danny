package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class LinkedListNthNodeTest {

  LinkedListNthNode listNthNode = new LinkedListNthNode();

  @Before
  public void setup() {
    listNthNode = new LinkedListNthNode();
  }

  @Test
  public void test() {
    ListNode node1 = new ListNode(1);
    ListNode node2 = new ListNode(2);
    ListNode node3 = new ListNode(3);
    ListNode node4 = new ListNode(4);
    ListNode node5 = new ListNode(5);

    node1.next = node2;
    node2.next = node3;
    node3.next = node4;
    node4.next = node5;

    int n = 2;
    ListNode resultNode = listNthNode.removeNthFromEnd(node1, n);

    assertEquals(1, resultNode.val);
    assertEquals(2, resultNode.next.val);
    assertEquals(3, resultNode.next.next.val);
    assertEquals(5, resultNode.next.next.next.val);
  }
}
