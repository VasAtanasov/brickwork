package com.github.vasatanasov.brickwork.utils;

public class Validator {

  public static boolean isOdd(int num) {
    return num % 2 != 0;
  }

  public static boolean isInvalidAreaParam(int i) {
    return i < 1 || i > 100;
  }

  private Validator() {
    throw new AssertionError();
  }
}
