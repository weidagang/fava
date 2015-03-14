package fava.data;

import static fava.Composing.compose;
import static fava.Folding.foldl;
import static fava.Folding.foldr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fava.Currying.F1;
import fava.Currying.F2;

/**
 * Functions for list operations.
 * 
 * @author dagang.wei (weidagang@gmail.com)
 */
public class Lists {
  /**
   * Curried function for appending an element to a list.
   *  
   * <p>append :: T -> [T] -> [T] 
   * 
   * @param <T> the type of the element
   * @param arg1 the element
   * @param arg2 the list
   */
  public static <T> F2<T, List<T>, List<T>> append() {
    return new F2<T, List<T>, List<T>>() {
      @Override
      public List<T> apply(T element, List<T> list) {
        ArrayList<T> result = new ArrayList<T>(list);
        result.add(element);
        return result;
      }
    };
  }

  /**
   * Curried function for reversing the elements in a list.
   * 
   * <p>reverse :: [T] -> [T]
   */
  public static <T> F1<List<T>, List<T>> reverse() {
    return foldr(Lists.<T>append(), new ArrayList<T>());
  }

  /**
   * Curried function for sorting a list.
   * 
   * <p>sort :: (T -> T -> Int) -> [T] -> [T] 
   */
  public static <T> F2<F2<T, T, Integer>, List<T>, List<T>> sort() {
    return new F2<F2<T, T, Integer>, List<T>, List<T>>() {
      @Override
      public List<T> apply(final F2<T, T, Integer> comparator, final List<T> list) {
        ArrayList<T> result = new ArrayList<T>(list);
        Collections.sort(result, new Comparator<T>() {
          @Override
          public int compare(T arg1, T arg2) {
            return comparator.apply(arg1, arg2);
          }
        });
        return result;
      }
    };
  }

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
    return Lists.<T, R>map().apply(f);
  }
}
