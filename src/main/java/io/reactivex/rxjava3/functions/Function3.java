package io.reactivex.rxjava3.functions;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * A functional interface (callback) that computes a value based on multiple input values.
 *
 * @param <T1> the first value type
 * @param <T2> the second value type
 * @param <T3> the third value type
 * @param <R> the result type
 */
@FunctionalInterface
public interface Function3<@NonNull T1, @NonNull T2, @NonNull T3, @NonNull R> {
  /**
   * Calculate a value based on the input values.
   *
   * @param t1 the first value
   * @param t2 the second value
   * @param t3 the third value
   * @return the result value
   * @throws Throwable if the implementation wishes to throw any type of exception
   */
  R apply(T1 t1, T2 t2, T3 t3) throws Throwable;
}
