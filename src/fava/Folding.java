package fava;

import java.util.List;

import fava.Currying.F1;

/**
 * Facilities for folding.
 * 
 * @author Dagang Wei (weidagang@gmail.com)
 */
public class Folding {
  /** Left fold: (T -> R -> R) -> R -> [T] -> R */
  public static <T, R> R foldl(F1<T, F1<R, R>> f, R r, List<T> data) {
    return foldl(f, r, data, 0);
  }

  /** Right fold: (T -> R -> R) -> R -> [T] -> R */
  public static <T, R> R foldr(F1<T, F1<R, R>> f, R r, List<T> data) {
    return foldr(f, r, data, 0);
  }

  private static <T, R> R foldl(F1<T, F1<R, R>> f, R r, List<T> data, int index) {
    return index == data.size() ? r : foldl(f, f.apply(data.get(index)).apply(r), data, index + 1);
  }

  private static <T, R> R foldr(F1<T, F1<R, R>> f, R r, List<T> data, int index) {
    return index == data.size() ? r : f.apply(data.get(index)).apply(foldr(f, r, data, index + 1));
  }
}
