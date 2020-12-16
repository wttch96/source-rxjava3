package io.reactivex.rxjava3.functions;

/** 返回布尔值的功能接口(回调). */
@FunctionalInterface
public interface BooleanSupplier {
  /**
   * Returns a boolean value.
   *
   * @return a boolean value
   * @throws Throwable if the implementation wishes to throw any type of exception
   */
  boolean getAsBoolean() throws Throwable; // NOPMD
}
