package io.reactivex.rxjava3.core;

import io.reactivex.rxjava3.annotations.NonNull;

/** 组合运算符使用的便捷接口和回调将{@link Completable}流畅地转换为另一个{@code Completable}. */
@FunctionalInterface
public interface CompletableTransformer {
  /**
   * 将函数应用于上游{@link Completable}并返回{@link CompletableSource}.
   *
   * @param upstream 上游{@code Completable}实例
   * @return 转换后的{@code CompletableSource}实例
   */
  @NonNull
  CompletableSource apply(@NonNull Completable upstream);
}
