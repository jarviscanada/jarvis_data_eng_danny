package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import org.junit.Test;

public class FindLargestSmallestTest {

  @Test
  public void test() {
    int[] testArr = new int[]{2, 3, 5, 7, 1};

    int[] result = FindLargestSmallest.oneLoop(testArr);

    assertEquals(1, result[0]);
    assertEquals(7, result[1]);

    result = FindLargestSmallest.withStreamApi(testArr);
    assertEquals(1, result[0]);
    assertEquals(7, result[1]);

    result = FindLargestSmallest.withCollectionApi(testArr);
    assertEquals(1, result[0]);
    assertEquals(7, result[1]);
  }
}
