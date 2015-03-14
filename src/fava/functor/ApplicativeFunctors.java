package fava.functor;

import fava.Currying.F2;

public class ApplicativeFunctors {
  public static <T1, T2, R> F2<ApplicativeFunctor<T1>, ApplicativeFunctor<T2>, ApplicativeFunctor<R>>
      liftA(final F2<T1, T2, R> f) {
    return new F2<ApplicativeFunctor<T1>, ApplicativeFunctor<T2>, ApplicativeFunctor<R>>() {
      @Override
      public ApplicativeFunctor<R> apply(ApplicativeFunctor<T1> arg1, ApplicativeFunctor<T2> arg2) {
        return arg1.liftA(f).apply(arg2);
      }
    };
  }
}
