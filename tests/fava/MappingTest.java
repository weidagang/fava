package fava;

import static fava.Mapping.map;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import fava.Currying.F1;

public class MappingTest {
  @Test
  public void testMap() {
    List<Integer> result = map(
        new F1<Integer, Integer>() {
          @Override
          public Integer apply(Integer arg) {
            return arg * arg;
          }
        },
        Arrays.asList(1, 2, 3));
    assertEquals(3, result.size());
    assertEquals(1, (int)result.get(0));
    assertEquals(4, (int)result.get(1));
    assertEquals(9, (int)result.get(2));
  }
}
