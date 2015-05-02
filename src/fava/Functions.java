package fava;

public final class Functions {
  /**
   * Functional interface for function of type {@code T -> R}.
   */
  public interface IF1<T, R> {
    R apply(T arg);
  }

  /**
   * Functional interface for function of type {@code T1 -> T2 -> R}.
   */
  public interface IF2<T1, T2, R> {
    R apply(T1 arg1, T2 arg2);
  }

  /**
   * Functional interface for function of type {@code T1 -> T2 -> T3 -> R}.
   */
  public interface IF3<T1, T2, T3, R> {
    R apply(T1 arg1, T2 arg2, T3 arg3);
  }

  /**
   * Functional interface for function of type {@code T1 -> T2 -> T3 -> T4 -> R}.
   */
  public interface IF4<T1, T2, T3, T4, R> {
    R apply(T1 arg1, T2 arg2, T3 arg3, T4 arg4);
  }

  /**
   * Functional interface for function of type {@code T1 -> T2 -> T3 -> T4 -> T5 -> R}.
   */
  public interface IF5<T1, T2, T3, T4, T5, R> {
    R apply(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5);
  }
}
