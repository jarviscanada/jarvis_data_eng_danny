package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class StringToIntegerTest {

  StringToInteger stringToInteger;

  @Before
  public void setup() {
    stringToInteger = new StringToInteger();
  }

  @Test
  public void test() {
    assertEquals(42, stringToInteger.atoiUsingParse("42"));
    assertEquals(-42, stringToInteger.atoiUsingParse("   -42"));

    assertEquals(42, stringToInteger.atoiUsingASCII("42"));
    assertEquals(-42, stringToInteger.atoiUsingASCII("   -42"));
  }
}
