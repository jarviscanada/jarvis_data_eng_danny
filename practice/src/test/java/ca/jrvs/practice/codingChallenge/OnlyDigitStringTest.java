package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class OnlyDigitStringTest {

  OnlyDigitString onlyDigitString;

  @Before
  public void setup() {
    onlyDigitString = new OnlyDigitString();
  }

  @Test
  public void test() {
    assertTrue(onlyDigitString.onlyDigitsASCII("1234"));
    assertFalse(onlyDigitString.onlyDigitsASCII("123,000"));

    assertTrue(onlyDigitString.onlyDigitsAPI("1234"));
    assertFalse(onlyDigitString.onlyDigitsAPI("123,000"));

    assertTrue(onlyDigitString.onlyDigitsRegex("1234"));
    assertFalse(onlyDigitString.onlyDigitsRegex("123,000"));
  }
}
