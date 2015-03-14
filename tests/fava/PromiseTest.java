package fava;

import static fava.Composing.compose;
import static fava.data.Lists.flatMap;
import static fava.data.Lists.map;
import static fava.data.Lists.sort;
import static fava.data.Strings.compareIgnoreCase;
import static fava.data.Strings.concat;
import static fava.data.Strings.join;
import static fava.data.Strings.split;
import static fava.data.Strings.toUpperCase;
import static fava.promise.Promises.fmap;
import static fava.promise.Promises.liftA;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import fava.Currying.F1;
import fava.Currying.F2;
import fava.data.Lists;
import fava.promise.Promise;

public class PromiseTest {
  private static final String URL1 = "http://www.a.com/a.htm";
  private static final String URL2 = "http://www.b.com/b.htm";
  private static final String URL3 = "http://www.c.com/c.htm";
  private static final String PAGE1 = "Hello world";
  private static final String PAGE2 = "I love programming in Java";
  private static final String PAGE3 = "Fava = Functional Java";

  @Test
  public void testPromise_fmap() throws Exception {
    F1<Promise<String>, Promise<String>> f =
        fmap(compose(split(" "), Lists.<String>reverse(), map(toUpperCase()), join().apply("_")));
    assertEquals("JAVA_IN_PROGRAMMING_LOVE_I", f.apply(promise(URL2)).get());
  }

  /**
   * Concatenates 2 web pages which are asynchronously fetched from the Internet.
   * 
   * <p>This test case demonstrates lifting a function of type "String -> String"
   * into a function of type "Promise<String> -> Promise<String>".
   */
  @Test
  public void testPromise_liftA() throws Exception {
    // Promise.liftA lifts concat into concatPromise. This allows us to abstract away
    // the asynchronous callbacks from the scene, consequently concatenating 2 asynchronous
    // strings looks the same as concatenating 2 regular strings.
    F2<Promise<String>, Promise<String>, Promise<String>> concatPromise = liftA(concat());
    Promise<String> page1AndPage2 = concatPromise.apply(promise(URL1), promise(URL2));
    assertEquals("Hello worldI love programming in Java", page1AndPage2.get());
  }

  /**
   * Fake HTTP promise for testing's purpose. It either returns a pre-configured
   * web page asynchronously or throws a "404 NOT FOUND" exception.
   */
  private static class HttpPromise extends Promise<String> {
    private static final HashMap<String, String> pages = new HashMap<String, String>();
    static {
      pages.put(URL1, PAGE1);
      pages.put(URL2, PAGE2);
    }

    public static HttpPromise promise(String url) {
      return new HttpPromise(url);
    }

    private HttpPromise(final String url) {
      // Simulate asynchronous HTTP request of 1 second with thread.
      new Thread(new Runnable() {
          @Override
          public void run() {
            try {
              Thread.sleep(1000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            if (pages.containsKey(url)) {
              HttpPromise.this.notifySuccess(pages.get(url));
            } else {
              HttpPromise.this.notifyFailure(new Exception("404 NOT FOUND"));
            }
          }
        })
        .start();
    }
  }

  private static HttpPromise promise(String url) {
    return HttpPromise.promise(url);
  }
}