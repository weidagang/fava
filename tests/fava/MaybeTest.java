package fava;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fava.Currying.F1;
import fava.data.Maybe;
import fava.data.Numbers;
import fava.functor.ApplicativeFunctors;
import fava.functor.Functors;

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
    Maybe<String> result = nothing.fmap(intToStr);
    assertFalse(result.hasValue());
  }

  @Test
  public void testJust() {
    Maybe<Integer> just3 = Maybe.just(3);
    Maybe<String> result = just3.fmap(intToStr);
    assertTrue(result.hasValue());
    assertEquals("3", result.getValue());
  }

  @Test
  public void testAdd() {
    Maybe<Integer> just3 = Maybe.just(3);
    Maybe<Integer> just4 = Maybe.just(4);
    assertEquals(Maybe.just(7), Maybe.fmap2(Numbers.add()).apply(just3, just4));
  }
}
