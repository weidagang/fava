package fava;

import fava.Currying.F1;

/**
 * Utilities for composing functions. 
 * 
 * @author dagang.wei (weidagang@gmail.com)
 */
public class Composing {
  /** 
   * Composes 2 functions into one function.
   * 
   * <p> compose :: (T -> U) -> (U -> R) 
   */
  public static <T, U, R> F1<T, R> compose(final F1<T, U> f1, final F1<U, R> f2) {
    return new F1<T, R>() {
      @Override
      public R apply(T arg) {
        return f2.apply(f1.apply(arg));
      }
    };
  }

  /**
   * Composes 3 functions into one function.
   * 
   * <p> compose(f1, f2, f3) = compose(compose(f1, f2), f3)
   */
  public static <T, U1, U2, R> F1<T, R> compose(final F1<T, U1> f1, final F1<U1, U2> f2, final F1<U2, R> f3) {
    return compose(compose(f1, f2), f3);
  }

  /**
   * Composes 4 functions into one function.
   * 
   * <p> compose(f1, f2, f3, f4) = compose(compose(f1, f2, f3), f4)
   */
  public static <T, U1, U2, U3, R> F1<T, R> compose(
      final F1<T, U1> f1, 
      final F1<U1, U2> f2, 
      final F1<U2, U3> f3,
      final F1<U3, R> f4) {
    return compose(compose(f1, f2, f3), f4);
  }
}
