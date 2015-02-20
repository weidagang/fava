package fava;

import java.util.ArrayList;
import java.util.List;

import fava.Currying.F2;

/**
 * Utilities for list operations.
 * 
 * @author Dagang Wei (weidagang@gmail.com)
 */
public class Lists {
  /** add :: T -> [T] -> [T] */
  public static <T> F2<T, List<T>, List<T>> _add() {
    return new F2<T, List<T>, List<T>>() {
      @Override
      public List<T> apply(T arg1, List<T> arg2) {
        ArrayList result = new ArrayList(arg2);
        result.add(arg1);
        return result;
      }
    };
  };
}
