package io.reactivex.rxjava3.functions;

/**消耗原始long值的功能接口(回调). */
@FunctionalInterface
public interface LongConsumer {
  /**
   * Consume a primitive long input.
   *
   * @param t the primitive long value
   * @throws Throwable if the implementation wishes to throw any type of exception
   */
  void accept(long t) throws Throwable;
}
