package fava;

import java.util.List;

import fava.Currying.F2;

/**
 * Facilities for folding.
 * 
 * @author Dagang Wei (weidagang@gmail.com)
 */
public class Folding {
  /** Left fold: (R -> T -> R) -> R -> [T] -> R */
  public static <T, R> R foldl(F2<R, T, R> f, R r, List<T> data) {
    return foldl(f, r, data, 0);
  }

  /** Right fold: (R -> T -> R) -> R -> [T] -> R */
  public static <T, R> R foldr(F2<R, T, R> f, R r, List<T> data) {
    return foldr(f, r, data, 0);
  }

  private static <T, R> R foldl(F2<R, T, R> f, R r, List<T> data, int index) {
    return index == data.size() ? r : foldl(f, f.apply(r, data.get(index)), data, index + 1);
  }

  private static <T, R> R foldr(F2<R, T, R> f, R r, List<T> data, int index) {
    return index == data.size() ? r : f.apply(foldr(f, r, data, index + 1), data.get(index));
  }
}
