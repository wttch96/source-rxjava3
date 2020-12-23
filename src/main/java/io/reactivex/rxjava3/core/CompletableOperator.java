package io.reactivex.rxjava3.core;

import io.reactivex.rxjava3.annotations.NonNull;

/** 用于将下游观察者映射/包装到上游观察者的接口. */
@FunctionalInterface
public interface CompletableOperator {
  /**
   * 将函数应用于子项{@link CompletableObserver}并返回新的父项{@code CompletableObserver}.
   *
   * @param observer 子{@code CompletableObserver}实例
   * @return 父{@code CompletableObserver}实例
   * @throws Throwable on failure
   */
  @NonNull
  CompletableObserver apply(@NonNull CompletableObserver observer) throws Throwable;
}
