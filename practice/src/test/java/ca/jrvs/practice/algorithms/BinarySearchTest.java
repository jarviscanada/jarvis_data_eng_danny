package ca.jrvs.practice.algorithms;

import static org.junit.Assert.*;

import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

public class BinarySearchTest {
  BinarySearch binarySearch = new BinarySearch();
  Integer[] arr;
  Integer target;

  @Before
  public void setup() {
    arr = new Integer[]{1,2,3,5,6,8,9,12,16};
    target = 3;
  }

  @Test
  public void testRecursion() {
    Optional<Integer> result = binarySearch.binarySearchRecursion(arr, target);
    assertEquals(target, result.get());
  }

  @Test
  public void testIterative() {
    Optional<Integer> result = binarySearch.binarySearchIteration(arr, target);
    assertEquals(target, result.get());
  }
}
