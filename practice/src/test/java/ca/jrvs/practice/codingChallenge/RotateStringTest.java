package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class RotateStringTest {

  RotateString rotateString;

  @Before
  public void setup() {
    rotateString = new RotateString();
  }

  @Test
  public void test() {
    assertTrue(rotateString.checkString("abcde", "cdeab"));
    assertFalse(rotateString.checkString("abcde", "abced"));
  }
}
