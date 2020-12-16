package io.reactivex.rxjava3.functions;

/** 具有单个cancel方法的功能接口， 可以抛出异常. */
@FunctionalInterface
public interface Cancellable {

  /**
   * Cancel the action or free a resource.
   *
   * @throws Throwable if the implementation wishes to throw any type of exception
   */
  void cancel() throws Throwable;
}
