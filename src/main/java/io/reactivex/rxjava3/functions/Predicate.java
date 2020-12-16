package io.reactivex.rxjava3.functions;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * 为给定输入值返回true或false的功能接口(回调).
 *
 * @param <T> the first value
 */
@FunctionalInterface
public interface Predicate<@NonNull T> {
  /**
   * Test the given input value and return a boolean.
   *
   * @param t the value
   * @return the boolean result
   * @throws Throwable if the implementation wishes to throw any type of exception
   */
  boolean test(T t) throws Throwable;
}
