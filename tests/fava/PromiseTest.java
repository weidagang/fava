package fava;

import static fava.Composing.compose;
import static fava.data.Lists.map;
import static fava.data.Strings.concat;
import static fava.data.Strings.join;
import static fava.data.Strings.split;
import static fava.data.Strings.toUpperCase;
import static fava.promise.Promise.failure;
import static fava.promise.Promise.unit;
import static fava.promise.Promises.fmap;
import static fava.promise.Promises.liftA;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import fava.Currying.F1;
import fava.Currying.F2;
import fava.Functions.IF1;
import fava.data.Lists;
import fava.data.Strings;
import fava.promise.Promise;
import fava.promise.Promises;

public class PromiseTest {
  private static final String URL1 = "http://www.a.com/a.htm";
  private static final String URL2 = "http://www.b.com/b.htm";
  private static final String URL3 = "http://www.c.com/c.htm";
  private static final String PAGE1 = "Hello world";
  private static final String PAGE2 = "I love programming in Java";
  private static final String PAGE3 = "Fava is Functional Java";

  @Test
  public void testPromise_fmap() throws Exception {
    F1<List<String>, List<String>> reverse = Lists.<String>reverse();
    F1<String, String> toUpperCase = toUpperCase();
    F1<String, String> convert = compose(split(" "), reverse, map(toUpperCase), join("_"));

    // fmap turns a function of type "T -> R" into a function of type "Promise<T> -> Promise<R>"
    F1<Promise<String>, Promise<String>> convertForPromise = fmap(convert);
    assertEquals("JAVA_IN_PROGRAMMING_LOVE_I", convertForPromise.apply(promise(URL2)).await());
  }

  /**
   * Tests functor law: fmap id = id
   */
  @Test
  public void testPromise_functorLaw1() {
    F1<Promise<String>, Promise<String>> id = Promises.fmap((IF1<String,String>)Identity::id);
    assertEquals(unit("foo"), id.apply(unit("foo")));
    assertEquals(failure(new RuntimeException("xxx")), id.apply(failure(new RuntimeException("xxx"))));
  }

  /**
   * Tests functor law: fmap (p . q) = (fmap p) . (fmap q)
   */
  @Test
  public void testPromise_functorLaw2() throws Exception {
    F1<String, List<String>> splitByComman = split(",");
    F1<List<String>, String> joinByUnderscore = join("_");
    String data = "I,love,Java";
    assertEquals(fmap(compose(splitByComman, joinByUnderscore)).apply(unit(data)).await(), "I_love_Java");
    assertEquals(
        fmap(compose(splitByComman, joinByUnderscore)).apply(unit(data)),
        compose(fmap(splitByComman), fmap(joinByUnderscore)).apply(unit(data)));
  }

  /**
   * This test case concatenates 2 web pages which are asynchronously fetched
   * from the Internet. It's to demonstrate lifting a function of type
   * "String -> String" into a function of type "Promise<String> -> Promise<String>".
   */
  @Test
  public void testPromise_liftA() throws Exception {
    // liftA lifts concat into concatPromise. This allows us to abstract away
    // the asynchronous callbacks from the scene, consequently concatenating 2 asynchronous
    // strings looks the same as concatenating 2 regular strings.
    F2<Promise<String>, Promise<String>, Promise<String>> concatPromise = liftA(concat());
    Promise<String> page1AndPage2 = concatPromise.apply(promise(URL1), promise(URL2));
    assertEquals("Hello worldI love programming in Java", page1AndPage2.await());
  }

  /**
   * This test case concatenates a list of web pages asynchronously fetched
   * from the Internet. It's to demonstrate lifting a function of type
   * "List<T> -> R" into a function of type "List<Promise<T>> -> Promise<R>"
   */
  @SuppressWarnings("unchecked")
  @Test
  public void testPromise_liftAForList() throws Exception {
    List<Promise<String>> promises = asList(promise(URL1), promise(URL2), promise(URL3));
    String r = liftA(join(",")).apply(promises).await();
    assertEquals(PAGE1 + "," + PAGE2 + "," + PAGE3, r);

    F1<List<Promise<String>>, Promise<String>> f = compose(
        Lists.map(Promises.fmap(split(" "))),
        Promises.liftA(Lists.<String>flatten()),
        Promises.fmap(Lists.<String>unique()),
        Promises.fmap(Lists.sort(Strings.compareIgnoreCase())),
        Promises.fmap(join(",")));
    String result = f.apply(Arrays.asList(promise(URL1), promise(URL2), promise(URL3))).await();
    System.out.println(result);

    F1<List<String>, Promise<String>> f2 = compose(
        Lists.map(promise()),
        Lists.map(Promises.fmap(split(" "))),
        Promises.liftA(Lists.<String>flatten()),
        Promises.fmap(compose(Lists.<String>unique(), Lists.sort(Strings.compareIgnoreCase()), join(","))));
    String result2 = f2.apply(Arrays.asList(URL1, URL2, URL3)).await();
    System.out.println(result2);
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
      pages.put(URL3, PAGE3);
    }

    public static HttpPromise promise(String url) {
      return new HttpPromise(url);
    }

    private HttpPromise(final String url) {
      final long interval = 100;
      // Simulate asynchronous HTTP request of 100 ms with thread.
      new Thread(new Runnable() {
          @Override
          public void run() {
            try {
              Thread.sleep(interval);
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

  private static Promise<String> promise(String url) {
    return HttpPromise.promise(url);
  }

  private static F1<String, Promise<String>> promise() {
    return new F1<String, Promise<String>>() {
      @Override
      public Promise<String> apply(String url) {
        return promise(url);
      }
    };
  }
}