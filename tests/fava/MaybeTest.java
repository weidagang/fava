package fava;

import static fava.data.Maybes.fmap;
import static fava.data.Maybes.liftA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fava.Currying.F1;
import fava.Currying.F2;
import fava.Functions.IF2;
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
  public void testLiftA_just() {
    Maybe<Integer> just3 = Maybe.just(3);
    Maybe<Integer> just4 = Maybe.just(4);
    IF2<Integer, Integer, Integer> add = Numbers::add;
    F2<Maybe<Integer>, Maybe<Integer>, Maybe<Integer>> addMaybe = liftA(add);
    assertEquals(Maybe.just(7), addMaybe.apply(just3, just4));
  }

  @Test
  public void testLiftA_nothing() {
    Maybe<Integer> just3 = Maybe.just(3);
    IF2<Integer, Integer, Integer> add = Numbers::add;
    F2<Maybe<Integer>, Maybe<Integer>, Maybe<Integer>> addMaybe = liftA(add);
    assertEquals(Maybe.nothing(), addMaybe.apply(just3, Maybe.nothing()));
  }
}
