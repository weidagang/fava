package fava;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fava.Currying.F1;
import fava.Currying.F2;
import fava.data.Strings;

public class CurryingTest {
  @Test
  public void testCurrying() {
    F2<Integer, String, String> times = Currying.<Integer, String, String>curry(Strings::times);
    assertEquals("abcabcabc", times.apply(3).apply("abc"));
    assertEquals("abcabcabc", times.apply(3).apply("abc"));
    assertTrue(times.apply(3) instanceof F1<?, ?>);
    assertEquals("abcabcabc", times.apply(3, "abc"));
  }
}
