package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class LinkedListCycleTest {

  LinkedListCycle linkedListCycle;

  @Before
  public void setup() {
    linkedListCycle = new LinkedListCycle();
  }

  @Test
  public void test1() {
    ListNode node1 = new ListNode(3);
    ListNode node2 = new ListNode(2);
    ListNode node3 = new ListNode(0);
    ListNode node4 = new ListNode(4);
    node1.next = node2;
    node2.next = node3;
    node3.next = node4;
    node4.next = node2;

    assertTrue(linkedListCycle.hasCycle(node1));
  }
}
