package fava;

import fava.Currying.F1;

/**
 * Utilities for composing functions. 
 * 
 * @author Dagang Wei (weidagang@gmail.com)
 */
public class Composing {
  /** 
   * Composes two functions into one function.
   * 
   * <p> compose :: (T -> U) -> (U -> R) 
   */
  public static <T, U, R> F1<T, R> compose(final F1<T, U> fn1, final F1<U, R> fn2) {
    return new F1<T, R>() {
      @Override
      public R apply(T arg) {
        return fn2.apply(fn1.apply(arg));
      }
    };
  }

  /**
   * Composes three functions into one function.
   * 
   * <p> compose(f1, f2, f3) = compose(compose(f1, f2), f3)
   */
  public static <T, U1, U2, R> F1<T, R> compose(final F1<T, U1> fn1, final F1<U1, U2> fn2, final F1<U2, R> fn3) {
    return compose(compose(fn1, fn2), fn3);
  }
}
