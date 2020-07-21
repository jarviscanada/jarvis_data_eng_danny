package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class FibonacciTest {

  Fibonacci fibonacci;
  int expected1, expected2, expected3,
      n1, n2, n3;

  @Before
  public void setup() {
    fibonacci = new Fibonacci();
    expected1 = 89;
    expected2 = 144;
    expected3 = 5;
    n1 = 11;
    n2 = 12;
    n3 = 5;
  }

  @Test
  public void testDynamic() {
    assertEquals(expected1, fibonacci.fib_dynamic(n1));
    assertEquals(expected2, fibonacci.fib_dynamic(n2));
    assertEquals(expected3, fibonacci.fib_dynamic(n3));
  }

  @Test
  public void testRecursion() {
    assertEquals(expected1, fibonacci.fib_recursion(n1));
    assertEquals(expected2, fibonacci.fib_recursion(n2));
    assertEquals(expected3, fibonacci.fib_recursion(n3));
  }
}
