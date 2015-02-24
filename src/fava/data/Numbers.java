package fava.data;

import fava.Currying.F2;

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
