package fava.monad;

import fava.Functions.IF1;

public interface Monad<T> {
  /**
   * bind :: Monad T -> (T -> R) -> Monad R
   */
  <R> Monad<R> bind(IF1<T, ? extends Monad<R>> f);
}