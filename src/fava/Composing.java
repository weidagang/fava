package fava;

import fava.Currying.F1;
import fava.Functions.IF1;

/**
 * Functions for functional composition. 
 * 
 * @author dagang.wei (weidagang@gmail.com)
 */
public class Composing {
  /** 
   * Composes 2 functions into one function.
   * 
   * <p>_ :: (T -> U) -> (U -> R) -> T -> R
   */
  public static <T, U, R> F1<T, R> _(final IF1<T, U> f1, final IF1<U, R> f2) {
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
   * <p>_(f1, f2, f3) = _(_(f1, f2), f3)
   */
  public static <T, U1, U2, R> F1<T, R> _(final IF1<T, U1> f1, final IF1<U1, U2> f2, final IF1<U2, R> f3) {
    return _(_(f1, f2), f3);
  }

  /**
   * Composes 4 functions into one function.
   * 
   * <p>_(f1, f2, f3, f4) = _(_(f1, f2, f3), f4)
   */
  public static <T, U1, U2, U3, R> F1<T, R> _(
      final IF1<T, U1> f1, 
      final IF1<U1, U2> f2, 
      final IF1<U2, U3> f3,
      final IF1<U3, R> f4) {
    return _(_(f1, f2, f3), f4);
  }

  /**
   * Composes 5 functions into one function.
   * 
   * <p>_(f1, f2, f3, f4, f5) = _(_(f1, f2, f3, f4), f5)
   */
  public static <T, U1, U2, U3, U4, R> F1<T, R> _(
      final IF1<T, U1> f1, 
      final IF1<U1, U2> f2, 
      final IF1<U2, U3> f3,
      final IF1<U3, U4> f4,
      final IF1<U4, R> f5) {
    return _(_(f1, f2, f3, f4), f5);
  }

  /**
   * Composes 6 functions into one function.
   * 
   * <p>_(f1, f2, f3, f4, f5, f6) = _(_(f1, f2, f3, f4, f5, f6)
   */
  public static <T, U1, U2, U3, U4, U5, R> F1<T, R> _(
      final IF1<T, U1> f1, 
      final IF1<U1, U2> f2, 
      final IF1<U2, U3> f3,
      final IF1<U3, U4> f4,
      final IF1<U4, U5> f5,
      final IF1<U5, R> f6) {
    return _(_(f1, f2, f3, f4, f5), f6);
  }
}
