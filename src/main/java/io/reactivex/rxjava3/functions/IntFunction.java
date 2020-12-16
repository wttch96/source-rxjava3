package io.reactivex.rxjava3.functions;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * 具有原始值和类型T的返回值的功能接口(回调).
 *
 * @param <T> the returned value type
 */
@FunctionalInterface
public interface IntFunction<@NonNull T> {
  /**
   * 根据原始整数输入计算值.
   *
   * @param i the input value
   * @return the result Object
   * @throws Throwable if the implementation wishes to throw any type of exception
   */
  T apply(int i) throws Throwable;
}
