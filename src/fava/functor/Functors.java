package fava.functor;

import fava.Currying.F1;

public class Functors {
  public static <T, R, FT extends Functor<T>, FR extends Functor<R>> F1<FT, FR> fmap(final F1<T, R> f) {
    return new F1<FT, FR>() {
      @SuppressWarnings("unchecked")
      @Override
      public FR apply(FT functor) {
        return (FR) functor.fmap(f);
      }
    };
  }
}
