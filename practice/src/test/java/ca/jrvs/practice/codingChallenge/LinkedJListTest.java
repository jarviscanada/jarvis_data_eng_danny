package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.assertFalse;

import java.util.Iterator;
import java.util.LinkedList;
import org.junit.Before;
import org.junit.Test;

public class LinkedJListTest {

  LinkedJList linkedJList;

  @Before
  public void setup() {
    linkedJList = new LinkedJList();
    linkedJList.add(1);
    linkedJList.add(3);
    linkedJList.add(5);
    linkedJList.add(1);
    linkedJList.add(2);
    linkedJList.add(5);
  }

  @Test
  public void test() {
    linkedJList.removeDuplicates();

    LinkedList<Integer> linkedList = linkedJList.getList();
    Iterator iterator = linkedList.iterator();

    assertFalse(linkedJList.hasDuplicates());
  }
}
