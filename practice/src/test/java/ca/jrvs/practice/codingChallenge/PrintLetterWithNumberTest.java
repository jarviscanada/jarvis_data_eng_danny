package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import org.junit.Test;

public class PrintLetterWithNumberTest {

  @Test
  public void test() {
    String input = "abceeA";
    String output = "a1b2c3e5e5A27";

    assertEquals(output, PrintLetterWithNumber.print(input));
  }
}
