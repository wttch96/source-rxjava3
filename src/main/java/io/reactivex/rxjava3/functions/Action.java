package io.reactivex.rxjava3.functions;

/** 一个类似于Runnable的功能接口, 但允许抛出一个受检异常. */
@FunctionalInterface
public interface Action {
  /**
   * 运行操作并有选择地抛出一个受检异常.
   *
   * @throws Throwable 如果实现希望抛出任何类型的异常
   */
  void run() throws Throwable;
}
