package fava;

import static fava.Composing._;
import static fava.data.Lists.map;
import static fava.data.Strings.join;
import static fava.data.Strings.split;
import static fava.data.Strings.toUpperCase;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fava.Currying.F1;
import fava.data.Lists;

public class ComposingTest {
  /**
   * This test case demonstrate composing the following functions:
   * 1) split by space;
   * 2) reverse a list;
   * 3)
   */
  @Test
  public void testCompose() {
    F1<String, String> f = _(split(" "), Lists.<String>reverse(), map(toUpperCase()), join("_"));
    assertEquals("JAVA_IN_PROGRAMMING_LOVE_I", f.apply("I love programming in Java"));
  }
}
