package io.reactivex.rxjava3.functions;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * 一个函数接口, 它接受一个值并返回另一个值, 该值可能具有不同的类型, 并允许引发已检查的异常.
 *
 * @param <T> the input value type
 * @param <R> the output value type
 */
@FunctionalInterface
public interface Function<@NonNull T, @NonNull R> {
  /**
   * Apply some calculation to the input value and return some other value.
   *
   * @param t the input value
   * @return the output value
   * @throws Throwable if the implementation wishes to throw any type of exception
   */
  R apply(T t) throws Throwable;
}
