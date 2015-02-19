package fava;

import java.util.ArrayList;
import java.util.List;

import fava.Currying.F1;
import fava.Currying.F2;

/**
 * List related utilities.

 * @author Dagang Wei (weidagang@gmail.com)
 */
public class Lists {
  public static <T> List<T> add(List<T> data, T e) {
    ArrayList<T> result = new ArrayList<T>(data);
    result.add(e);
    return result;
  }

  public static <T> F1<T, List<T>> addTo(final List<T> data) {
    return new F1<T, List<T>>() {
      @Override
      public List<T> apply(T e) {
        return add(data, e);
      }
    };
  }

  public static <T> F2<List<T>, T, List<T>> add() {
    return new F2<List<T>, T, List<T>>() {
      @Override
      public List<T> apply(List<T> data, T e) {
        return add(data, e);
      }
    };
  }
}
