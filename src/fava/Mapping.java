package fava;

import static fava.Composing.compose;
import static fava.Folding.foldl;

import java.util.ArrayList;
import java.util.List;

import fava.Currying.F1;
import fava.Currying.F2;
import fava.data.Lists;

/**
 * Functions for mapping.
 *
 * @author dagang.wei (weidagang@gmail.com)
 * @see @see <a href="https://en.wikipedia.org/wiki/Map_(higher-order_function)">Map (higher-order function)</a>
 */
public class Mapping {
  /**
   * Maps a function over the elements of a list.
   *  
   * <p> map: (T -> R) -> [T] -> [R]
   * 
   * @param <T> the type of elements in the source list
   * @param <R> the type of elements in the result list
   * @param f the function to be mapped over the elements of the list
   * @param list the list
   * 
   */
  public static <T, R> List<R> map(final F1<T, R> f, List<T> list) {
    final ArrayList<R> result = new ArrayList<R>(list.size());
    // Here we define {@code map} with {@code foldl}, that means {@code fold}
    // is more fundamental than {@code map} in the level of abstraction. 
    return foldl(compose(f, Lists.<R>append()), result, list);
  }

  /**
   * Curried function for mapping a function {@code f} over the elements of a list.
   * 
   * <p> map: (T -> R) -> [T] -> [R]
   * 
   * @param <T> the type of elements in the source list
   * @param <R> the type of elements in the result list
   * @param arg1 the function to be mapped over the elements of the list
   * @param arg2 the list
   */
  public static <T, R> F2<F1<T, R>, List<T>, List<R>> map() {
    return new F2<F1<T, R>, List<T>, List<R>>() {
      @Override
      public List<R> apply(F1<T, R> f, List<T> list) {
        return map(f, list);
      }
    };
  }

  /**
   * Curried function for mapping a function {@code f} over the elements of a list with {@code f} bound.
   * 
   * <p> map: (T -> R) -> [T] -> [R]
   * 
   * @param <T> the type of elements in the source list
   * @param <R> the type of elements in the result list
   * @param arg1 the function to be mapped over the elements of the list
   * @param arg2 the list
   */
  public static <T, R> F1<List<T>, List<R>> map(F1<T, R> f) {
    return Mapping.<T, R>map().apply(f);
  }
}
