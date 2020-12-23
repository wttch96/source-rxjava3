package io.reactivex.rxjava3.core;

import io.reactivex.rxjava3.annotations.NonNull;

/** 具有{@code subscription()}方法的功能接口, 该方法接收 {@link CompletableEmitter}实例的实例, 该实例允许以取消安全的方式推送事件. */
@FunctionalInterface
public interface CompletableOnSubscribe {

  /**
   * 为每个订阅的{@link CompletableObserver}调用.
   *
   * @param emitter 安全的发射器实例, 切勿{@code null}
   * @throws Throwable on error
   */
  void subscribe(@NonNull CompletableEmitter emitter) throws Throwable;
}
