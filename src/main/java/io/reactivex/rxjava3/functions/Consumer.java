package io.reactivex.rxjava3.functions;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * 接受单个值的功能接口(回调), 可以抛出异常.
 *
 * @param <T> the value type
 */
@FunctionalInterface
public interface Consumer<@NonNull T> {
  /**
   * Consume the given value.
   *
   * @param t the value
   * @throws Throwable if the implementation wishes to throw any type of exception
   */
  void accept(T t) throws Throwable;
}
