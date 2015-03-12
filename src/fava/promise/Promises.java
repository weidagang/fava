package fava.promise;

import fava.Currying.F1;
import fava.promise.Promise.Listener;

public class Promises {
  public static <T, R> F1<Promise<T>, Promise<R>> fmap(final F1<T, R> f) {
    return new F1<Promise<T>, Promise<R>>() {
      @Override
      public Promise<R> apply(Promise<T> promiseT) {
        final Promise<R> promiseR = new Promise<R>() {};

        promiseT.addListener(new Listener<T>() {
          @Override
          public void onSuccess(T value) {
            promiseR.notifySuccess(f.apply(value));
          }

          @Override
          public void onFailure(Exception exception) {
            promiseR.notifyFailure(exception);
          }
        });

        return promiseR;
      }
    };
  }
}
