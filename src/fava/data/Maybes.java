package fava.data;

import static fava.Composing.compose;
import static fava.Currying.curry;
import static fava.Currying.uncurry;
import fava.Currying.F1;
import fava.Currying.F2;
import fava.Functions.IF1;
import fava.Functions.IF2;

/**
 * A set of functions for {@link Maybe}.
 */
public final class Maybes {
  /**
   * {@code fmap} for Maybe Functor, which lifts a function of {@code T -> R}
   * into a function of type {@code Maybe<T> -> Maybe<R>}.
   */
  public static <T, R> F1<Maybe<T>, Maybe<R>> fmap(final IF1<T, R> f) {
    return new F1<Maybe<T>, Maybe<R>>() {
      @Override
      public Maybe<R> apply(Maybe<T> maybeT) {
        return maybeT.fmap(f);
      }
    };
  }

  /**
   * {@code fapply} for Maybe Applicative Functor, which turns an instance of
   * type {@code Maybe<T -> R>} into a function of type {@code Maybe<T> -> Maybe<R>}.
   */
  public static <T, R, F extends IF1<T, R>> F1<Maybe<T>, Maybe<R>> fapply(final Maybe<F> f) {
    assert f.hasValue();
    return fmap(f.getValue());
  }

  /**
   * Lifts a function of type {@code T1 -> T2 -> R} into a function of type
   * {@code Maybe<T1> -> Maybe<T2> -> Maybe<R>}.
   */
  public static <T1, T2, R> F2<Maybe<T1>, Maybe<T2>, Maybe<R>> liftA(IF2<T1, T2, R> f) {
    IF1<Maybe<F1<T2, R>>, F1<Maybe<T2>, Maybe<R>>> fapply =
        (IF1<Maybe<F1<T2, R>>, F1<Maybe<T2>, Maybe<R>>>)Maybes::fapply;
    return uncurry(compose(fmap(curry(f)), fapply));
  }
}
