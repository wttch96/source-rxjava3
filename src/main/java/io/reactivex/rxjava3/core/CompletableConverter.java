package io.reactivex.rxjava3.core;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * {@link Completable＃to}运算符使用的便利接口和回调函数, 可以将Completable流畅地转换为另一个值.
 *
 * <p>History: 2.1.7 - experimental
 *
 * @param <R> the output type
 * @since 2.2
 */
@FunctionalInterface
public interface CompletableConverter<@NonNull R> {
  /**
   * 将函数应用于上游Completable并返回类型为{@code R}的转换值.
   *
   * @param upstream 上游Completable实例
   * @return 转换后的值
   */
  R apply(@NonNull Completable upstream);
}
