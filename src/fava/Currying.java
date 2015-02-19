package fava;

/**
 * Function types and facilities to simulate currying in Java.
 * 
 * @author Dagang Wei (weidagang@gmail.com)
 */
public final class Currying {
  /** Function of type: T -> R */
  public static interface F1<T, R> {
    R apply(T arg);
  }

  /** Function of type: T1 -> T2 -> R */
  public static abstract class F2<T1, T2, R> {
    public abstract R apply(T1 arg1, T2 arg2);

    /** Partial application: (T1 -> T2 -> R) -> T1 -> (T2 -> R) */
    public F1<T2, R> apply(final T1 arg1) {
      return new F1<T2, R>() {
        @Override
        public R apply(T2 arg2) {
          return F2.this.apply(arg1, arg2);
        }
      };
    }
  }

  /** Function of type: T1 -> T2 -> T3 -> R */
  public static abstract class F3<T1, T2, T3, R> {
    public abstract R apply(T1 arg1, T2 arg2, T3 arg3);

    /** Partial application: (T1 -> T2 -> T3 -> R) -> T1 -> (T2 -> T3 -> R) */
    public F2<T2, T3, R> apply(final T1 arg1) {
      return new F2<T2, T3, R>() {
        @Override
        public R apply(T2 arg2, T3 arg3) {
          return F3.this.apply(arg1, arg2, arg3);
        }
      };
    }
  }
}
