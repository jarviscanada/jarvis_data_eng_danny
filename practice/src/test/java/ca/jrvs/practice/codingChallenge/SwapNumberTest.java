package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import org.junit.Test;

public class SwapNumberTest {

  @Test
  public void testBit() {
    int[] testArr = new int[]{3,2};
    int[] result = SwapNumber.swapBit(testArr);
    assertEquals(2, result[0]);
    assertEquals(3, result[1]);
  }

  @Test
  public void testArithmetic() {
    int[] testArr = new int[]{3,2};
    int[] result = SwapNumber.swapArithmetic(testArr);
    assertEquals(2, result[0]);
    assertEquals(3, result[1]);
  }
}
