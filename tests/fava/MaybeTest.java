package fava;

import static fava.data.Maybe.fmap;
import static fava.data.Maybe.liftA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fava.Currying.F1;
import fava.data.Maybe;
import fava.data.Numbers;

public class MaybeTest {
  F1<Integer, String> intToStr  = new F1<Integer, String>() {
    @Override
    public String apply(Integer arg) {
      return String.valueOf(arg);
    }
  };

  @Test
  public void testNothing() {
    Maybe<Integer> nothing = Maybe.nothing();
    Maybe<String> result = fmap(intToStr).apply(nothing);
    assertFalse(result.hasValue());
  }

  @Test
  public void testJust() {
    Maybe<Integer> just3 = Maybe.just(3);
    Maybe<String> result = fmap(intToStr).apply(just3);
    assertTrue(result.hasValue());
    assertEquals("3", result.getValue());
  }

  @Test
  public void testAdd() {
    Maybe<Integer> just3 = Maybe.just(3);
    Maybe<Integer> just4 = Maybe.just(4);
    assertEquals(Maybe.just(7), liftA(Numbers.add()).apply(just3, just4));
  }
}
