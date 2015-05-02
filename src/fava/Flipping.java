package fava;

import fava.Currying.F2;
import fava.Functions.IF2;

public class Flipping {
  public static <T, U, R> F2<U, T, R> flip(IF2<T, U, R> f) {
    return new F2<U, T, R>() {
      @Override
      public R apply(U arg1, T arg2) {
        return f.apply(arg2, arg1);
      }
    };
  }
}
