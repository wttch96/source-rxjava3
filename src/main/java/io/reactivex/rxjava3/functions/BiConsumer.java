package io.reactivex.rxjava3.functions;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * 接受两个值(可能不同类型)的功能接口(回调).
 *
 * @param <T1> the first value type
 * @param <T2> the second value type
 */
@FunctionalInterface
public interface BiConsumer<@NonNull T1, @NonNull T2> {

  /**
   * Performs an operation on the given values.
   *
   * @param t1 the first value
   * @param t2 the second value
   * @throws Throwable if the implementation wishes to throw any type of exception
   */
  void accept(T1 t1, T2 t2) throws Throwable;
}
