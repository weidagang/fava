package fava.functor;

import fava.Functions.IF1;

public interface Functor<T> {
  <R> Functor<R> fmap(IF1<T, R> f);
}