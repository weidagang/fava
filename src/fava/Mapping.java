package fava;

import static fava.Composing.compose;
import static fava.Folding.foldl;
import static fava.Lists.addTo;

import java.util.ArrayList;
import java.util.List;

import fava.Currying.F1;
import fava.Currying.F2;

/**
 * Facilities for mapping.
 * @author Dagang Wei (weidagang@gmail.com)
 */
public class Mapping {
  /** Map function: (T -> R) -> [T] -> [R] */
  public static <T, R> List<R> map(final F1<T, R> f, List<T> data) {
    final ArrayList<R> result = new ArrayList<R>(data.size());
     // {@code fold} is more fundamental than {@code map}, here we define {@code map} with foldl. 
    return foldl(
        new F2<List<R>, T, List<R>>() {
          @Override
          public List<R> apply(List<R> r, T e) {
            r.add(f.apply(e));
            return r;
          }
        },
        result,
        data);
  }
}
