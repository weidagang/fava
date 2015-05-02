package fava.promise;

import java.util.ArrayList;

import fava.Currying.F1;
import fava.functor.Functor;

/**
 * A promise with type parameter T represents a value of type T that may be
 * available asynchronously in the future. Users of a promise get the value
 * or failure info by registering listeners.
 * 
 * <p>This class is intended to be inherited by subclasses to provide specific
 * asynchronous values, such as asynchronous HTTP response or asynchronous
 * database query result.
 * 
 * @author dagang.wei (weidagang@gmail.com)
 */
public class Promise<T> implements Functor<T> {
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

  private State state = State.PENDING;
  private T value;
  private Exception exception;
  private ArrayList<Listener<T>> listeners = new ArrayList<Listener<T>>();

  /**
   * Lifts a value into a promise.
   */
  public static <T> Promise<T> successOf(T value) {
    Promise<T> promise = new Promise<T>();
    promise.state = State.SUCCEEDED;
    promise.value = value;
    return promise;
  }

  /**
   * Lifts a failure into a promise.
   */
  public static <T> Promise<T> failureOf(Exception exception) {
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
   * Await until the promise is fulfilled or rejected, then returns the value
   * or throws the exception.
   */
  public T await() throws Exception {
    while (state == State.PENDING) {
      try {
        Thread.sleep(1); //TODO: change the implementation later.
      } catch (Exception e) {
      }
    }

    if (state == State.SUCCEEDED) {
      return value;
    }

    throw exception;
  }

  /**
   * Returns the value.
   * 
   * <p>Precondition: state == State.SUCCEEDED
   */
  public T getValue() {
    assert state == State.SUCCEEDED;
    return value;
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

  @Override
  public <R> Promise<R> fmap(F1<T, R> f) {
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
}
