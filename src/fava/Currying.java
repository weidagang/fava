package fava;

import fava.Functions.IF1;
import fava.Functions.IF2;
import fava.Functions.IF3;

/**
 * Functions for currying.
 * 
 * @author dagang.wei (weidagang@gmail.com)
 */
public final class Currying {
  /** 
   * Function of type T -> R.
   */
  public static abstract class F1<T, R> implements IF1<T, R> {
    @Override
    public abstract R apply(final T arg);

    public final R _(final T arg) {
      return apply(arg);
    }
  }

  /** 
   * Function of type T1 -> T2 -> R.
   * 
   * <p> This class implements the {@code F1<T1, F1<T2, R>>} interface, meaning a {@code F2}
   * instance is a curried function, it will return another function when partially applied.
   * 
   * <p> Subclasses only need to implement the 2 arguments version of {@code apply}, then the
   * curried version will be available automatically.
   */
  public static abstract class F2<T1, T2, R> extends F1<T1, F1<T2, R>> implements IF2<T1, T2, R> {
    @Override
    public abstract R apply(T1 arg1, T2 arg2);

    public final R _(T1 arg1, T2 arg2) {
      return apply(arg1, arg2);
    }

    @Override
    public final F1<T2, R> apply(final T1 arg1) {
      return new F1<T2, R>() {
        @Override
        public R apply(T2 arg2) {
          return F2.this.apply(arg1, arg2);
        };
      };
    }
  }

  /** 
   * Function of type T1 -> T2 -> T3 -> R.
   * 
   * <p> This class implements the {@code F1<T1, F2<T2, T3, R>>} interface, meaning a {@code F3}
   * instance is a curried function, it will return another function when partially applied.
   * 
   * <p> Subclasses only need to implement the 3 arguments version of {@code apply}, then the
   * curried version will be available automatically.
   */
  public static abstract class F3<T1, T2, T3, R> extends F1<T1, F2<T2, T3, R>> implements IF3<T1, T2, T3, R> {
    @Override
    public abstract R apply(T1 arg1, T2 arg2, T3 arg3);

    public final R _(T1 arg1, T2 arg2, T3 arg3) {
      return apply(arg1, arg2, arg3);
    }

    @Override
    public final F2<T2, T3, R> apply(final T1 arg1) {
      return new F2<T2, T3, R>() {
        @Override
        public R apply(T2 arg2, T3 arg3) {
          return F3.this.apply(arg1, arg2, arg3);
        }
      };
    }
  }

  /**
   * Unary operator of type T -> T, used as a short form of F1<T, T>.
   */
  public static abstract class P1<T> extends F1<T, T> {
  }

  /** 
   * Binary operator of type T -> T -> T, used as a short form of F2<T, T, T>.
   */
  public static abstract class P2<T> extends F2<T, T, T> {
  }

  /** 
   * 3-ary operator of type T -> T -> T -> T, used as a short form of F3<T, T, T, T>.
   */
  public static abstract class P3<T> extends F3<T, T, T, T> {
  }

  /**
   * Turns a function into the curried form.
   */
  public static <T1, T2, R> F2<T1, T2, R> curry(final IF2<T1, T2, R> f) {
    assert f != null;

    return new F2<T1, T2, R>() {
      @Override
      public R apply(T1 arg1, T2 arg2) {
        return f.apply(arg1, arg2);
      }
    };
  }

  /**
   * Turns a function into the curried form.
   */
  public static <T1, T2, T3, R> F3<T1, T2, T3, R> curry(final IF3<T1, T2, T3, R> f) {
    assert f != null;

    return new F3<T1, T2, T3, R>() {
      @Override
      public R apply(T1 arg1, T2 arg2, T3 arg3) {
        return f.apply(arg1, arg2, arg3);
      }
    };
  }

  /**
   * Turns a curried function which accepts only one argument each time into a function which
   * can accept 2 arguments each time. It is used for better readability. For example,
   * 
   * <p>{@code f2.apply(arg1, arg2)} is better than {@code f.apply(arg1).apply(arg2)}, in terms
   * of readability. 
   */
  public static <T1, T2, R> F2<T1, T2, R> uncurry(final F1<T1, F1<T2, R>> f) {
    return new F2<T1, T2, R>() {
      @Override
      public R apply(T1 arg1, T2 arg2) {
        return f.apply(arg1).apply(arg2);
      }
    };
  }
}
