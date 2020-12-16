package io.reactivex.rxjava3.core;

/** 使用onBackpressureBuffer时处理缓冲区溢出的选项. */
public enum BackpressureOverflowStrategy {
  /**
   * 发出{@link io.reactivex.rxjava3.exceptions.MissingBackpressureException
   * MissingBackpressureException}信号并终止序列.
   */
  ERROR,
  /** 从缓冲区中删除最早的值. */
  DROP_OLDEST,
  /** 从缓冲区中删除最新值. */
  DROP_LATEST
}
