package fava;

import fava.Currying.F1;
import fava.Currying.F2;

/**
 * Facilities to composing functions 
 * 
 * @author Dagang Wei (weidagang@gmail.com)
 */
public class Composing {
  public static <T, U, R> F1<T, R> compose(final F1<T, U> fn1, final F1<U, R> fn2) {
    return new F1<T, R>() {
      @Override
      public R apply(T arg) {
        return fn2.apply(fn1.apply(arg));
      }
    };
  }

  public static <T, U1, U2, R> F1<T, R> compose(final F1<T, U1> fn1, final F1<U1, U2> fn2, final F1<U2, R> fn3) {
    return compose(compose(fn1, fn2), fn3);
  }

  public static <T1, T2, U, R> F2<T1, T2, R> compose(final F2<T1, T2, U> fn1, final F1<U, R> fn2) {
    return new F2<T1, T2, R>() {
      @Override
      public R apply(T1 arg1, T2 arg2) {
        return fn2.apply(fn1.apply(arg1, arg2));
      }
    };
  }

}
