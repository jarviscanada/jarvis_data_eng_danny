package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class LinkedListMidNodeTest {

  LinkedListMidNode midNode;

  @Before
  public void setup() {
    midNode = new LinkedListMidNode();
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

    ListNode resultNode = midNode.middleNode(node1);
    assertEquals(3, resultNode.val);
    assertEquals(4, resultNode.next.val);
    assertEquals(5, resultNode.next.next.val);
  }
}
