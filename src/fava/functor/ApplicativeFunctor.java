package fava.functor;

import fava.Currying.F1;
import fava.Currying.F2;

public interface ApplicativeFunctor<T1> {
  <T2, R, A2 extends ApplicativeFunctor<T2>, AR extends ApplicativeFunctor<R>> F1<A2, AR> liftA(F2<T1, T2, R> f);
}