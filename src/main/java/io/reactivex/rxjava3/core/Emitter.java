package io.reactivex.rxjava3.core;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * 基本接口, 用于在各种类似于发生器的源操作程序(创建,生成)中以推送方式发射信号.
 *
 * <p>请注意, 通过{@link Emitter}实例提供给函数的{@link Emitter＃onNext}, {@link Emitter＃onError}和{@link
 * Emitter＃onComplete}方法应被同步调用, 而不是同时调用. 不支持从多个线程调用它们, 并导致未定义的行为.
 *
 * @param <T> 发射的数据信号的类型
 */
public interface Emitter<@NonNull T> {

  /**
   * 发出正常值信号.
   *
   * @param value 要发出信号的值， 而不是{@code null}
   */
  void onNext(@NonNull T value);

  /**
   * 发出{@link Throwable}异常的信号.
   *
   * @param error {@code Throwable}发出信号， 而不是{@code null}
   */
  void onError(@NonNull Throwable error);

  /** 发出完成信号. */
  void onComplete();
}
