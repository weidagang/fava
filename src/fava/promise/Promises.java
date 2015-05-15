package fava.promise;

import static fava.Currying.curry;

import java.util.List;

import fava.Composing;
import fava.Currying.F1;
import fava.Currying.F2;
import fava.Functions.IF1;
import fava.Functions.IF2;
import fava.data.Lists;
import fava.promise.Promise.Listener;

/**
 * A set of functions for {@link Promise}.
 */
public class Promises {
  /**
   * Lifts a function of type T -> R into a function of type Promise<T> -> Promise<R>.
   * It is the function form of {@link Promise<T>::fmap} for composability.
   */
  public static <T, R> Promise<R> fmap(IF1<T, R> f, Promise<T> promiseT) {
    return promiseT.fmap(f);
  }

  /**
   * The curried form of {@link Promises.fmap(F1<T, R>, Promise<T>)}.
   */
  public static <T, R> F2<IF1<T, R>, Promise<T>, Promise<R>> fmap() {
    return curry((IF2<IF1<T,R>, Promise<T>, Promise<R>>)Promises::fmap);
  }

  /**
   * The curried form of {@link Promises.fmap(F1<T, R>, Promise<T>)}.
   */
  public static <T, R> F1<Promise<T>, Promise<R>> fmap(IF1<T, R> f) {
    return Promises.<T, R>fmap().apply(f);
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

  /**
   * Flattens a promise of promise.
   * 
   * join :: Promise<Promise<T>> -> Promise<T>
   */
  public static <T> Promise<T> join(final Promise<Promise<T>> promiseOfPromiseT) {
    assert promiseOfPromiseT != null;

    final Promise<T> promiseT = new Promise<T>();

    promiseOfPromiseT.addListener(new Listener<Promise<T>>() {
      @Override
      public void onSuccess(Promise<T> p) {
        p.addListener(new Listener<T>(){
          @Override
          public void onSuccess(T value) {
            promiseT.notifySuccess(value);
          }

          @Override
          public void onFailure(Exception exception) {
            promiseT.notifyFailure(exception);
          }
        });
      }

      @Override
      public void onFailure(Exception exception) {
        promiseT.notifyFailure(exception);
      }
    });

    return promiseT;
  }

  /**
   * Flat-maps a function of type {@code T -> Promise<R>} over an instance of {@code Promise<T>}.
   *
   * <p>flatMap :: (T -> Promise R) -> Promise T -> Promise R
   */
  public static <T, R> Promise<R> flatMap(IF1<T, Promise<R>> f, Promise<T> promiseT) {
    return promiseT.bind(f);
  }

  /**
   * Curried form of the {@link flatMap(IF1<T, Promise<R>>, Promise<T>)}. This function turns a
   * function of type {@code T -> Promise<R>} into a function of type {@code Promise T -> Promise R}.
   */
  public static <T, R> F1<Promise<T>, Promise<R>> flatMap(IF1<T, Promise<R>> f) {
    return curry((IF2<IF1<T, Promise<R>>, Promise<T>, Promise<R>>)Promises::flatMap).apply(f);
  }

  /**
   * Gets value of the promise.
   */
  public static <T> T getValue(Promise<T> promise) {
    return promise.getValue();
  }

  /**
   * Curried form of the {@link getValue(Promise<T>)}.
   */
  public static <T> IF1<Promise<T>, T> getValue() {
    return Promise<T>::getValue;
  }
}
