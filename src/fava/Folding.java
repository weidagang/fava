package fava;

import java.util.List;

import fava.Currying.F1;
import fava.Currying.F2;
import fava.Currying.F3;

/**
 * Functions for folding (a.k.a reducing).
 * 
 * @author dagang.wei (weidagang@gmail.com)
 * @see <a href="https://en.wikipedia.org/wiki/Fold_(higher-order_function)">Fold (higher-order function)</a>
 */
public class Folding {
  /**
   * Left fold.
   * 
   * <p> foldl: (T -> R -> R) -> R -> [T] -> R 
   *
   * @param <T> the type of elements in the list
   * @param <R> the type of the folding result
   * @param f the folding function
   * @param initial the initial value
   * @param list the list to be folded
   */
  public static <T, R> R foldl(F1<T, F1<R, R>> f, R initial, List<T> list) {
    return Folding.<T, R>foldl().apply(f).apply(initial).apply(list);
  }

  /** 
   * Curried left fold.
   * 
   * <p> foldl: (T -> R -> R) -> R -> [T] -> R 
   *
   * @param <T> the type of elements in the list
   * @param <R> the type of the folding result
   * @param arg1 the folding function
   * @param arg2 the initial value
   * @param arg3 the list to be folded
   */
  public static <T, R> F3<F1<T, F1<R, R>>, R, List<T>, R> foldl() {
    return new F3<F1<T, F1<R, R>>, R, List<T>, R>() {
      @Override
      public R apply(F1<T, F1<R, R>> f, R initial, List<T> list) {
        return foldl(f, initial, list, 0);
      }
    };
  }

  /**
   * Curried left fold with the first argument bound.
   * 
   * <p> foldl: (T -> R -> R) -> R -> [T] -> R
   *
   * @param <T> the type of elements in the list
   * @param <R> the type of the folding result
   */
  public static <T, R> F2<R, List<T>, R> foldl(F1<T, F1<R, R>> f) {
    return Folding.<T, R>foldl().apply(f);
  }

  /**
   * Curried left fold with the first 2 arguments bound.
   * 
   * <p> foldl: (T -> R -> R) -> R -> [T] -> R 
   *
   * @param <T> the type of elements in the list
   * @param <R> the type of the folding result
   */
  public static <T, R> F1<List<T>, R> foldl(F1<T, F1<R, R>> f, R initial) {
    return Folding.<T, R>foldl().apply(f).apply(initial);
  }

  /**
   * Right fold: (T -> R -> R) -> R -> [T] -> R
   * 
   * @param <T> the type of elements in the list
   * @param <R> the type of the folding result
   * @param f the folding function
   * @param initial the initial value
   * @param list the list to be folded
   */
  public static <T, R> R foldr(F1<T, F1<R, R>> f, R r, List<T> list) {
    return foldr(f, r, list, 0);
  }

  /** 
   * Curried right fold.
   * 
   * <p> foldl: (T -> R -> R) -> R -> [T] -> R 
   *
   * @param <T> the type of elements in the list
   * @param <R> the type of the folding result
   * @param arg1 the folding function
   * @param arg2 the initial value
   * @param arg3 the list to be folded
   */
  public static <T, R> F3<F1<T, F1<R, R>>, R, List<T>, R> foldr() {
    return new F3<F1<T, F1<R, R>>, R, List<T>, R>() {
      @Override
      public R apply(F1<T, F1<R, R>> f, R r, List<T> data) {
        return foldr(f, r, data, 0);
      }
    };
  }

  /**
   * Curried right fold with the first argument bound.
   * 
   * <p> foldl: (T -> R -> R) -> R -> [T] -> R 
   *
   * @param <T> the type of elements in the list
   * @param <R> the type of the folding result
   */
  public static <T, R> F2<R, List<T>, R> foldr(F1<T, F1<R, R>> f) {
    return Folding.<T, R>foldr().apply(f);
  }

  /**
   * Curried right fold with the first 2 arguments bound.
   * 
   * <p> foldl: (T -> R -> R) -> R -> [T] -> R 
   *
   * @param <T> the type of elements in the list
   * @param <R> the type of the folding result
   */
  public static <T, R> F1<List<T>, R> foldr(F1<T, F1<R, R>> f, R r) {
    return Folding.<T, R>foldr().apply(f).apply(r);
  }

  private static <T, R> R foldl(F1<T, F1<R, R>> f, R r, List<T> data, int index) {
    return index == data.size() ? r : foldl(f, f.apply(data.get(index)).apply(r), data, index + 1);
  }

  private static <T, R> R foldr(F1<T, F1<R, R>> f, R r, List<T> data, int index) {
    return index == data.size() ? r : f.apply(data.get(index)).apply(foldr(f, r, data, index + 1));
  }
}
