package fava;

import static fava.Composing.compose;
import static fava.Mapping.map;
import static fava.data.Strings.join;
import static fava.data.Strings.split;
import static fava.data.Strings.toUpperCase;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

import fava.Currying.F1;
import fava.data.Lists;
import fava.promise.Promise;
import fava.promise.Promises;


public class PromiseTest {
  private static final String URL1 = "http://www.example.com/a.htm";
  private static final String URL2 = "http://www.example.com/b.htm";
  private static final String PAGE1 = "Hello world";
  private static final String PAGE2 = "I love programming in Java";

  @Test
  public void testHttpPromise() throws Exception {
    F1<String, String> f1 = compose(split(" "), Lists.<String>reverse(), map(toUpperCase()), join().apply("_"));
    F1<Promise<String>, Promise<String>> f2 = Promises.fmap(f1);
    HttpPromise promise2 = HttpPromise.of(URL2);
    assertEquals("JAVA_IN_PROGRAMMING_LOVE_I", f2.apply(promise2).get());
  }

  private static class HttpPromise extends Promise<String> {
    private static final HashMap<String, String> pages = new HashMap<String, String>();
    static {
      pages.put(URL1, PAGE1);
      pages.put(URL2, PAGE2);
    }

    public static HttpPromise of(String url) {
      return new HttpPromise(url);
    }

    /**
     * Fake HTTP promise for testing's purpose.
     */
    private HttpPromise(final String url) {
      // Simulate asynchronous HTTP request of 2 seconds with thread.
      new Thread(new Runnable() {
          @Override
          public void run() {
            try {
              Thread.sleep(2000);
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
}