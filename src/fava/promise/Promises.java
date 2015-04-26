package fava.promise;

import java.util.List;

import fava.Currying.F1;
import fava.Currying.F2;
import fava.data.Lists;
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

  public static <T1, T2, R> F2<Promise<T1>, Promise<T2>, Promise<R>> liftA(final F2<T1, T2, R> f) {
    return new F2<Promise<T1>, Promise<T2>, Promise<R>>() {
      private Promise.State state1 = Promise.State.PENDING;
      private Promise.State state2 = Promise.State.PENDING;
      private T1 value1;
      private T2 value2;

      @Override
      public Promise<R> apply(Promise<T1> promiseT1, Promise<T2> promiseT2) {
        final Promise<R> promiseR = new Promise<R>() {};

        promiseT1.addListener(new Listener<T1>() {
          @Override
          public void onSuccess(T1 value) {
            value1 = value;
            state1 = Promise.State.SUCCEEDED;
            synchronized(promiseR) {
              if (state2 == Promise.State.SUCCEEDED) {
                promiseR.notifySuccess(f.apply(value1, value2));
              }
            }
          }

          @Override
          public void onFailure(Exception exception) {
            promiseR.notifyFailure(exception);
            synchronized(promiseR) {
              if (state2 != Promise.State.FAILED) {
                promiseR.notifyFailure(exception); // Only notify failure once.
              }
            }
          }
        });

        promiseT2.addListener(new Listener<T2>() {
          @Override
          public void onSuccess(T2 value) {
            value2 = value;
            state2 = Promise.State.SUCCEEDED;
            synchronized(promiseR) {
              if (state1 == Promise.State.SUCCEEDED) {
                promiseR.notifySuccess(f.apply(value1, value2));
              }
            }
          }

          @Override
          public void onFailure(Exception exception) {
            promiseR.notifyFailure(exception);
            synchronized(promiseR) {
              if (state1 != Promise.State.FAILED) {
                promiseR.notifyFailure(exception); // Only notify failure once.
              }
            }
          }
        });

        return promiseR;
      }
    };
  }

  public static <T, R> F1<List<Promise<T>>, Promise<R>> liftA(final F1<List<T>, R> f) {
    return new F1<List<Promise<T>>, Promise<R>>() {
      @Override
      public Promise<R> apply(final List<Promise<T>> promisesT) {
        final Promise<R> promiseR = new Promise<R>() {};
        for (Promise<T> promiseT : promisesT) {
          promiseT.addListener(new Listener<T>() {
            @Override
            public void onSuccess(T value) {
              notifyIfDone(promisesT, promiseR);
            }

            @Override
            public void onFailure(Exception exception) {
              notifyIfDone(promisesT, promiseR);
            }
          });
        }
        return promiseR;
      }

      private void notifyIfDone(List<Promise<T>> promisesT, Promise<R> promiseR) {
        Promise.State state = getState(promisesT);
        if (state == Promise.State.SUCCEEDED) {
          R value = f.apply(Lists.map(Promises.<T>getValue(), promisesT));
          promiseR.notifySuccess(value);
        } else if (state == Promise.State.FAILED) {
          promiseR.notifyFailure(new Exception());
        }
      }

      private Promise.State getState(List<Promise<T>> promises) {
        boolean hasFailure = false;
        for (Promise<?> promise : promises) {
          if (promise.state() == Promise.State.PENDING) {
            return Promise.State.PENDING;
          }
          if (promise.state() == Promise.State.FAILED) {
            hasFailure = true;
          }
        }
        return hasFailure ? Promise.State.FAILED : Promise.State.SUCCEEDED;
      }
    };
  }

  public static <T> F1<Promise<T>, T> getValue() {
    return new F1<Promise<T>, T>() {
      @Override
      public T apply(Promise<T> promise) {
        return promise.getValue();
      }
    };
  }
}
