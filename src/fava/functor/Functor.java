package fava.functor;

import fava.Currying.F1;

public interface Functor<T> {
  <R> Functor<R> fmap(F1<T, R> f);
}