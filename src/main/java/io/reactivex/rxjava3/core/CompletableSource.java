package io.reactivex.rxjava3.core;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * 表示基本的{@link Completable}源基本接口, 可通过{@link CompletableObserver}进行使用.
 *
 * @since 2.0
 */
@FunctionalInterface
public interface CompletableSource {

  /**
   * 将给定的{@link CompletableObserver}订阅到此{@code CompletableSource}实例.
   *
   * @param observer {@code CompletableObserver}, 而不是{@code null}
   * @throws NullPointerException 如果{@code observer}为{@code null}
   */
  void subscribe(@NonNull CompletableObserver observer);
}
