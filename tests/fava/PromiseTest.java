package fava;

import static fava.Composing.compose;
import static fava.data.Lists.map;
import static fava.data.Strings.concat;
import static fava.data.Strings.join;
import static fava.data.Strings.split;
import static fava.data.Strings.toUpperCase;
import static fava.promise.Promises.fmap;
import static fava.promise.Promises.fmap2;
import static org.junit.Assert.assertEquals;

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

  @Test
  public void testPromise_fmap2() throws Exception {
    F2<Promise<String>, Promise<String>, Promise<String>> f = fmap2(concat());
    Promise<String> result = f.apply(promise(URL1), promise(URL2));
    assertEquals("Hello worldI love programming in Java", result.get());
  }

  @Test
  public void testPromise() throws Exception {
    Lists.<String, List<String>>map(split(" "));
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