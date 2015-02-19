package fava;

import static fava.Folding.foldl;
import static fava.Composing.compose;
import static fava.Lists.addTo;
import java.util.ArrayList;
import java.util.List;

import fava.Currying.F1;

/**
 * Facilities for mapping.

 * @author Dagang Wei (weidagang@gmail.com)
 */
public class Mapping {
  public static <T, R> List<R> map(final F1<T, R> f, List<T> data) {
    final ArrayList<R> result = new ArrayList<R>(data.size());
    return foldl(
        compose(f, addTo(result)),
        result,
        data);
  }
}
