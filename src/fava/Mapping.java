package fava;

import static fava.Composing.compose;
import static fava.Folding.foldl;

import java.util.ArrayList;
import java.util.List;

import fava.Currying.F1;

/**
 * Facilities for mapping.
 *
 * @author Dagang Wei (weidagang@gmail.com)
 */
public class Mapping {
  /** Map function: (T -> R) -> [T] -> [R] */
  public static <T, R> List<R> map(final F1<T, R> f, List<T> data) {
    final ArrayList<R> result = new ArrayList<R>(data.size());
    // Here we define {@code map} with {@code foldl}, that means {@code fold}
    // is more fundamental than {@code map} in the level of abstraction. 
    return foldl(compose(f, Lists.<R>_add()), result, data);
  }
}
