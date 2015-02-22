package fava;

import static fava.Composing.compose;
import static fava.Lists.reverseStringList;
import static fava.Mapping.map;
import static fava.Strings.join;
import static fava.Strings.split;
import static fava.Strings.toUpperCase;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fava.Currying.F1;

public class ComposingTest {
  @Test
  public void testComposing() {
    F1<String, String> f = compose(split(" "), reverseStringList(), map(toUpperCase()), join().apply("_"));
    assertEquals("JAVA_IN_PROGRAMMING_LOVE_I", f.apply("I love programming in Java"));
  }
}
