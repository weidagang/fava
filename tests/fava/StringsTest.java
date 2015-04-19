package fava;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fava.data.Strings;

public class StringsTest {

  @Test
  public void testTimes() {
    assertEquals("abcabcabc", Strings.times()._(3)._("abc"));
  }
}
