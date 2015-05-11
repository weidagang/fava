package fava.promise;

import java.util.ArrayList;

import fava.Functions.IF1;
import fava.functor.Functor;
import fava.monad.Monad;

/**
 * An instance of {@code Promise<T>} represents a value of type T that may be
 * available asynchronously in the future. Users of a promise get the value
 * or failure info by registering listeners.
 * 
 * <p>This class is intended to be inherited by subclasses to provide specific
 * asynchronous values, such as asynchronous HTTP response or asynchronous
 * database query result.
 * 
 * @author dagang.wei (weidagang@gmail.com)
 */
public class Promise<T> implements Functor<T>, Monad<T> {
  /**
   * States of a promise.
   */
  public static enum State {
    PENDING,
    SUCCEEDED,
    FAILED,
  }

  /**
   * Listener to get the value or failure info in the future.
   */
  public static interface Listener<T> {
    void onSuccess(T value);
    void onFailure(Exception exception);
  }

  protected State state = State.PENDING;
  protected T value;
  protected Exception exception;
  protected ArrayList<Listener<T>> listeners = new ArrayList<Listener<T>>();

  /**
   * Lifts a value into a promise.
   */
  public static <T> Promise<T> unit(T value) {
    Promise<T> promise = new Promise<T>();
    promise.state = State.SUCCEEDED;
    promise.value = value;
    return promise;
  }

  /**
   * Lifts a failure into a promise.
   */
  public static <T> Promise<T> failure(Exception exception) {
    Promise<T> promise = new Promise<T>();
    promise.state = State.FAILED;
    promise.exception = exception;
    return promise;
  }

  /**
   * Returns the current state of the promise.
   */
  public State state() {
    return state;
  }

  /**
   * Awaits until the promise is fulfilled or rejected.
   * 
   * @return the value if succeeded, or null if failed.
   */
  public T await() {
    while (state == State.PENDING) {
      try {
        Thread.sleep(1); //TODO: change the implementation later.
      } catch (Exception e) {
      }
    }
    return state == State.SUCCEEDED ? value : null;
  }

  /**
   * Gets the value of this promise. If the promise failed, this method calls
   * {@link failureToValue()} to get the default value.
   * 
   * <p>Precondition: state == SUCCEEDED || state == FAILED
   */
  public T getValue() {
    assert state == State.PENDING || state == State.FAILED;
    return state == State.SUCCEEDED ? value: failureToValue();
  }

  /**
   * Returns the exception.
   *
   * <p>Precondition: state == State.FAILED
   */
  public Exception getException() {
    assert state == State.FAILED;
    return exception;
  }

  /**
   * Adds a listener to the promise. If the current state is PENDING, the listener
   * will be called later on when the promise gets fulfilled to rejected. Otherwise,
   * the listener will be called immediately.
   */
  public final void addListener(Listener<T> listener) {
    if (state == State.SUCCEEDED) {
      listener.onSuccess(value);
    } else if (state == State.FAILED) {
      listener.onFailure(exception);
    } else if (state == State.PENDING) {
      listeners.add(listener);
    }
  }

  @Override
  public <R> Promise<R> fmap(IF1<T, R> f) {
    final Promise<R> promiseR = new Promise<R>() {};

    this.addListener(new Listener<T>() {
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

  @Override
  public <R> Promise<R> bind(IF1<T, ? extends Monad<R>> f) {
    // promiseR is the composition of "this" promise and "that promise.
    final Promise<R> promiseR = new Promise<R>() {};

    // callback for "this" promise 
    this.addListener(new Listener<T>() {
      @Override
      public void onSuccess(T value) {
        Promise<R> that = (Promise<R>)f.apply(value);
        assert that != null;
        // callback for "that" promise
        that.addListener(new Listener<R>() {
          @Override
          public void onSuccess(R value) {
            promiseR.notifySuccess(value);
          }

          @Override
          public void onFailure(Exception exception) {
            promiseR.notifyFailure(exception);
          }
        });
      }

      @Override
      public void onFailure(Exception exception) {
        promiseR.notifyFailure(exception);
      }
    });

    return promiseR;
  }

  @Override
  public boolean equals(Object obj) {
      if (!(obj instanceof Promise<?>)) {
        return false;
      }

      Promise<?> that = (Promise<?>)obj;
      Object v1 = this.await();
      Object v2 = that.await();

      return this.state == that.state
          ? (v1 != null ? v1.equals(v2) : (v2 == null))
          : false;
  }

  /**
   * Fulfills the promise, moves the state from PENDING to SUCCEEDED. It's intended
   * to be called inside of subclasses. 
   */
  protected final void notifySuccess(T value) {
    assert state == State.PENDING;

    this.value = value;
    this.state = State.SUCCEEDED;
    for (Listener<T> listener : listeners) {
      listener.onSuccess(value);
    }
  }

  /**
   * Rejects the promise, moves the state from PENDING to FAILED. It's intended to be
   * called inside of subclasses.
   */
  protected final void notifyFailure(Exception exception) {
    assert state == State.PENDING;

    this.exception = exception;
    this.state = State.FAILED;
    for (Listener<T> listener : listeners) {
      listener.onFailure(exception);
    }
  }

  /**
   * Gets the value for the failure case. If the promise failed, the default
   * implementation is returning null, but subclasses can override it to return
   * any default value or just throw an exception.
   * 
   * <p>Precondition: state == FAILED
   * 
   * @return the corresponding value for the failure.
   */
  protected T failureToValue() {
    assert state == State.FAILED;
    return null;
  }
}
