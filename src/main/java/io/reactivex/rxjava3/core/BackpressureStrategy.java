package io.reactivex.rxjava3.core;

/** 表示将反压应用于源序列的选项. */
public enum BackpressureStrategy {
  /**
   * 编写{@code onNext}事件时不会进行任何缓冲或删除. 下游必须处理任何溢出.
   *
   * <p>当将一个自定义参数应用在onBackpressureXXX运算符上时很有用.
   */
  MISSING,
  /**
   * 发出信号{@link io.reactivex.rxjava3.exceptions.MissingBackpressureException
   * MissingBackpressureException}以防下游无法跟上.
   */
  ERROR,
  /** 缓冲<em>all</em> {@code onNext}的值, 直到下游消耗它. */
  BUFFER,
  /** 如果下游无法跟上, 则丢弃最新的{@code onNext}值. */
  DROP,
  /** 仅保留最新的{@code onNext}值, 如果下游跟不上, 则覆盖以前的任何值. */
  LATEST
}
