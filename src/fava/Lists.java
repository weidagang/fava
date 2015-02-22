package fava;

import static fava.Folding.foldr;

import java.util.ArrayList;
import java.util.List;

import fava.Currying.F1;
import fava.Currying.F2;

/**
 * Utilities for list operations.
 * 
 * @author Dagang Wei (weidagang@gmail.com)
 */
public class Lists {
  /**
   * Curried function for appending an element to a list.
   *  
   * <p> append :: T -> [T] -> [T] 
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
   * <p> reverse :: [T] -> [T]
   */
  public static <T> F1<List<T>, List<T>> reverse() {
    return foldr(Lists.<T>append(), new ArrayList<T>());
  }

  /**
   * Alias for Lists.<String>reverse
   */
  public static F1<List<String>, List<String>> reverseStringList() {
    return Lists.<String>reverse();
  }
}
