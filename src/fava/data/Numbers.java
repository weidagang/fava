package fava.data;

import fava.Currying.F2;

/**
 * Functions for numbers.
 * 
 * @author dagang.wei (weidagang@gmail.com)
 */
public class Numbers {
  public static F2<Integer, Integer, Integer> add() {
    return new F2<Integer, Integer, Integer>() {
      @Override
      public Integer apply(Integer arg1, Integer arg2) {
        return arg1 + arg2;
      }
    };
  }
}
