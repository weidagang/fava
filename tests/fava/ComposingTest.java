package fava;

import static fava.Composing.compose;
import static fava.data.Lists.map;
import static fava.data.Strings.join;
import static fava.data.Strings.split;
import static fava.data.Strings.toUpperCase;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fava.Currying.F1;
import fava.data.Lists;

public class ComposingTest {
  @Test
  public void testCompose() {
    F1<String, String> f = compose(split(" "), Lists.<String>reverse(), map(toUpperCase()), join("_"));
    assertEquals("JAVA_IN_PROGRAMMING_LOVE_I", f.apply("I love programming in Java"));
  }
}
