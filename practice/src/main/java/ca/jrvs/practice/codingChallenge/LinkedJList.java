package ca.jrvs.practice.codingChallenge;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * https://www.notion.so/Duplicate-LinkedList-Node-9600b06bc93d482899981f878851bf83
 */
public class LinkedJList {
  LinkedList<Integer> linkedList = new LinkedList<>();

  public LinkedJList() { }

  public void add(Integer i) {
    linkedList.add(i);
  }

  public LinkedList<Integer> getList() {
    return linkedList;
  }

  /**
   * Big-O: O(n)
   * Space complexity: O(n)
   * Justification: Iterates once over problem set, hashSet.contains is O(1) operation,
   * hash set can go up to size n
   */
  public void removeDuplicates() {
    Iterator iterator = linkedList.iterator();
    HashSet<Integer> hashSet = new HashSet<>();

    while(iterator.hasNext()) {
      Integer curr = (Integer)iterator.next();
      if (hashSet.contains(curr)) {
        iterator.remove();
      } else {
        hashSet.add(curr);
      }
    }
  }
}
