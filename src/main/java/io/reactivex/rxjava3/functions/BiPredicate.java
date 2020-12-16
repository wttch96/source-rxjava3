package io.reactivex.rxjava3.functions;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * 一个针对给定输入值返回true或false的功能接口(回调).
 *
 * @param <T1> the first value
 * @param <T2> the second value
 */
@FunctionalInterface
public interface BiPredicate<@NonNull T1, @NonNull T2> {

  /**
   * Test the given input values and return a boolean.
   *
   * @param t1 the first value
   * @param t2 the second value
   * @return the boolean result
   * @throws Throwable if the implementation wishes to throw any type of exception
   */
  boolean test(@NonNull T1 t1, @NonNull T2 t2) throws Throwable;
}
