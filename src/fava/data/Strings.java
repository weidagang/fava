package fava.data;

import static fava.Currying.uncurry;
import static fava.Folding.foldl;

import java.util.Arrays;
import java.util.List;

import fava.Currying.F1;
import fava.Currying.F2;

/** Utilities for string */
public class Strings {
  /**
   * Curried function for splitting a string by a delimiter.
   * 
   * <p> split :: String -> String -> [String]
   * 
   * @param arg1 the delimiter
   * @param arg2 the string to be splitted
   */
  public static F2<String, String, List<String>> split() {
    return new F2<String, String, List<String>>() {
      @Override public List<String> apply(String delimiter, String data) {
        return Arrays.asList(data.split(delimiter));
      }
    };
  }

  /**
   * Curried function for splitting a string by a delimiter with the first argument bound.
   * 
   * <p> splitBy :: String -> String -> [String]
   * 
   * @param arg1 the delimiter
   * @param arg2 the string to be splitted
   */
  public static F1<String, List<String>> split(String delimiter) {
    return split().apply(delimiter);
  }

  /**
   * Curried function for concatenating two strings.
   */
  public static F2<String, String, String> concat() {
    return new F2<String, String, String>() {
      @Override
      public String apply(String arg1, String arg2) {
        return arg1 + arg2;
      }
    };
  }

  /**
   * Curried function for joining a list of string by a delimiter.
   * 
   * <p> join :: String -> [String] -> String
   * 
   * @param arg1 the delimiter
   * @param arg2 the string list
   */
  public static F2<String, List<String>, String> join() {
    F1<String, F1<List<String>, String>> joinF = new F1<String, F1<List<String>, String>>() {
      @Override public F1<List<String>, String> apply(final String delimiter) {
        String initial = "";
        final F2<String, String, String> f = new F2<String, String, String>() {
          @Override public String apply(String e, String r) {
            return r.length() == 0 ? e : r + delimiter + e;
          }
        };
        return foldl(f, initial);
      }
    };
    
    return uncurry(joinF);
  }

  /**
   * Curried function for converting a string to upper case. 
   * 
   * <p> toUpperCase :: String -> String
   * 
   * @param str the string to be converted
   */
  public static F1<String, String> toUpperCase() {
    return new F1<String, String>() {
      @Override public String apply(String str) {
        return str.toUpperCase();
      }
    };
  }

  /**
   * Curried function for converting a string to lower case. 
   * 
   * <p> toLowerCase :: String -> String
   * 
   * @param str the string to be converted
   */
  public static F1<String, String> toLowerCase() {
    return new F1<String, String>() {
      @Override public String apply(String str) {
        return str.toLowerCase();
      }
    };
  }
}
