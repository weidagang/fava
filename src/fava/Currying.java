package fava;

/**
 * Utilities for currying.
 * 
 * @author Dagang Wei (weidagang@gmail.com)
 */
public final class Currying {
  /** 
   * Function of type: T -> R
   */
  public static interface F1<T, R> {
    R apply(final T arg);
  }

  /** 
   * Function of type: T1 -> T2 -> R
   * 
   * <p> This class implements the {@code F1<T1, F1<T2, R>>} interface, meaning a {@code F2}
   * instance is a curried function, it will return another function when partially applied.
   * 
   * <p> Subclasses of this class only need to implement the 2 arguments version of {@code apply},
   * then the curried version will be available automatically.
   */
  public static abstract class F2<T1, T2, R> implements F1<T1, F1<T2, R>> {
    public abstract R apply(T1 arg1, T2 arg2);

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
   * Function of type: T1 -> T2 -> T3 -> R
   * 
   * <p> This class implements the {@code F1<T1, F2<T2, T3, R>>} interface, meaning a {@code F3}
   * instance is a curried function, it will return another function when partially applied.
   * 
   * <p> Subclasses of this class only need to implement the 3 arguments version of {@code apply},
   * then the curried version will be available automatically.
   */
  public static abstract class F3<T1, T2, T3, R> implements F1<T1, F2<T2, T3, R>> {
    public abstract R apply(T1 arg1, T2 arg2, T3 arg3);

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
   * Casts {@code T1 -> (T2 -> R)} to {@code T1 -> T2 -> R}.
   */
  public static <T1, T2, R> F2<T1, T2, R> cast(final F1<T1, F1<T2, R>> f) {
    return new F2<T1, T2, R>() {
      @Override
      public R apply(T1 arg1, T2 arg2) {
        return f.apply(arg1).apply(arg2);
      }
    };
  }
}
