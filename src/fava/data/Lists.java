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
 * Functions for lists.
 * 
 * @author dagang.wei (weidagang@gmail.com)
 */
public class Lists {
  /**
   * Appends an element to a list. This function will not change the
   * original list, it will create a new list instead.
   *  
   * <p>append :: T -> [T] -> [T] 
   * 
   * @param <T> the type of the element
   * @param element the element
   * @param list the list
   */
  public static <T> List<T> append(T element, List<T> list) {
    ArrayList<T> result = new ArrayList<T>(list);
    result.add(element);
    return result;
  }

  /**
   * Curried version of append.
   *
   * <p>append :: T -> [T] -> [T] 
   */
  public static <T> F2<T, List<T>, List<T>> append() {
    return new F2<T, List<T>, List<T>>() {
      @Override
      public List<T> apply(T element, List<T> list) {
        return append(element, list);
      }
    };
  }

  /**
   * Flattens a list of lists into a single list.
   *
   * <p>flatten: [[T]] -> [T]
   */
  public static <T> List<T> flatten(final List<List<T>> listOfLists) {
    List<T> result = new ArrayList<T>();
    for (List<T> list : listOfLists) {
      result.addAll(list);
    }
    return result;
  }

  /**
   * Curried version of flatten.
   *
   * <p>flatten: [[T]] -> [T]
   */
  public static <T> F1<List<List<T>>, List<T>> flatten() {
    return new F1<List<List<T>>, List<T>>() {
      @Override
      public List<T> apply(List<List<T>> listOfLists) {
        return flatten(listOfLists);
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
   * Sorts a list.
   *
   * <p>sort :: (T -> T -> Int) -> [T] -> [T]
   */
  public static <T> List<T> sort(final F2<T, T, Integer> comparator, final List<T> list) {
    ArrayList<T> result = new ArrayList<T>(list);
    Collections.sort(result, new Comparator<T>() {
      @Override
      public int compare(T arg1, T arg2) {
        return comparator.apply(arg1, arg2);
      }
    });
    return result;
  }

  /**
   * Curried version of sort.
   *
   * <p>sort :: (T -> T -> Int) -> [T] -> [T]
   */
  public static <T> F2<F2<T, T, Integer>, List<T>, List<T>> sort() {
    return new F2<F2<T, T, Integer>, List<T>, List<T>>() {
      @Override
      public List<T> apply(final F2<T, T, Integer> comparator, final List<T> list) {
        return sort(comparator, list);
      }
    };
  }

  /**
   * Curried version of sort with partial application serving as syntax sugar.
   *
   * <p>sort :: (T -> T -> Int) -> [T] -> [T]
   */
  public static <T> F1<List<T>, List<T>> sort(F2<T, T, Integer> comparator) {
    return Lists.<T>sort().apply(comparator);
  }

  /**
   * Maps a function over the elements of a list.
   *
   * <p> map: (T -> R) -> [T] -> [R]
   *
   * @param <T> the type of elements in the source list
   * @param <R> the type of elements in the result list
   * @param f the function to be applied to each element of the list
   * @param list the list to be mapped over
   */
  public static <T, R> List<R> map(final F1<T, R> f, List<T> list) {
    final ArrayList<R> result = new ArrayList<R>(list.size());
    // Here we define {@code map} with {@code foldl}, that means {@code fold}
    // is more fundamental than {@code map} in the level of abstraction. 
    return foldl(compose(f, Lists.<R>append()), result, list);
  }

  /**
   * Curried version of mapn.
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
   * Curried version of map with partial application serving as syntax sugar.
   */
  public static <T, R> F1<List<T>, List<R>> map(F1<T, R> f) {
    return Lists.<T, R>map().apply(f);
  }

  /**
   * Maps a function of type "T -> [R]" over the elements of a list, then
   * flatten the results into a single list of type [R].
   *
   * <p> map: (T -> [R]) -> [T] -> [R]
   *
   * @param <T> the type of elements in the source list
   * @param <R> the type of elements in the result list
   * @param f the function to be applied to each element of the list
   * @param list the list to be mapped over
   */
  public static <T, R> List<R> flatMap(final F1<T, List<R>> f, List<T> list) {
    return flatten(map(f, list));
  }

  /**
   * Curried version of flatMap.
   */
  public static <T, R> F2<F1<T, List<R>>, List<T>, List<R>> flatMap() {
    return new F2<F1<T, List<R>>, List<T>, List<R>>() {
      @Override
      public List<R> apply(F1<T, List<R>> arg1, List<T> arg2) {
        return flatMap(arg1, arg2);
      }
    };
  }

  /**
   * Curried version of flatMap with partial application serving as syntax sugar.
   */
  public static <T, R> F1<List<T>, List<R>> flatMap(F1<T, List<R>> f) {
    return Lists.<T, R>flatMap().apply(f);
  }
}
