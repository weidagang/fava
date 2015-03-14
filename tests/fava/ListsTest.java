package fava;

import static fava.data.Lists.map;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import fava.Currying.F1;
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

  @Test
  public void testMap() {
    F1<List<Integer>, List<Integer>> squareOverList = map(
        new F1<Integer, Integer>() {
          @Override
          public Integer apply(Integer arg) {
            return arg * arg;
          }
        }
    );
    List<Integer> result = squareOverList.apply(Arrays.asList(1, 2, 3));
    assertEquals(3, result.size());
    assertEquals(1, (int)result.get(0));
    assertEquals(4, (int)result.get(1));
    assertEquals(9, (int)result.get(2));
  }
}
