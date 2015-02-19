package fava;

import static fava.Folding.foldl;
import static fava.Folding.foldr;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import fava.Currying.F2;

public class FoldingTest {
  @Test
  public void testFoldl() {
    F2<String, Integer, String> addParenthese = new F2<String, Integer, String>() {
      @Override
      public String apply(String r, Integer e) {
        return "(" + r + "+" + e + ")";
      }
    };
    assertEquals("(((0+1)+2)+3)", foldl(addParenthese, "0", Arrays.asList(1, 2, 3)));
  }

  @Test
  public void testFoldr() {
    F2<String, Integer, String> addParenthese = new F2<String, Integer, String>() {
      @Override
      public String apply(String r, Integer e) {
        return "(" + e + "+" + r + ")";
      }
    };
    assertEquals("(1+(2+(3+0)))", foldr(addParenthese, "0", Arrays.asList(1, 2, 3)));
  }
}
