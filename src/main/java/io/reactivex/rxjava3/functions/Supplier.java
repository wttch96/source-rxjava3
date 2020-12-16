package io.reactivex.rxjava3.functions;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * 提供单个值或引发异常的功能接口(回调).
 *
 * <p>已添加此接口, 以允许抛出{@link Throwable}的任何子类, 这对于Java标准{@link java.util.concurrent.Callable}接口是不可能的.
 *
 * @param <T> the value type returned
 * @since 3.0.0
 */
@FunctionalInterface
public interface Supplier<@NonNull T> {

  /**
   * Produces a value or throws an exception.
   *
   * @return the value produced
   * @throws Throwable if the implementation wishes to throw any type of exception
   */
  T get() throws Throwable;
}
