package io.reactivex.rxjava3.functions;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * 一个基于多个输入值来计算值的功能接口(回调).
 *
 * @param <T1> the first value type
 * @param <T2> the second value type
 * @param <R> the result type
 */
@FunctionalInterface
public interface BiFunction<@NonNull T1, @NonNull T2, @NonNull R> {

  /**
   * Calculate a value based on the input values.
   *
   * @param t1 the first value
   * @param t2 the second value
   * @return the result value
   * @throws Throwable if the implementation wishes to throw any type of exception
   */
  R apply(T1 t1, T2 t2) throws Throwable;
}
