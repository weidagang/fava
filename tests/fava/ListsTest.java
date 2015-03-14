package fava;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import fava.data.Lists;
import fava.data.Strings;

public class ListsTest {
  @Test
  public void testSort() {
    List<String> languages = Arrays.asList("java", "Haskell", "C++", "basic", "Lisp", "python");
    List<String> sorted = Lists.<String>sort().apply(Strings.compareIgnoreCase()).apply(languages);
    List<String> expected = Arrays.asList("basic", "C++", "Haskell", "java", "Lisp", "python");
    assertEquals(expected.size(), sorted.size());
    for (int i = 0; i < sorted.size(); i++) {
      assertEquals(expected.get(i), sorted.get(i));
    }
  }
}
