package io.reactivex.rxjava3.core;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 提供一种机制, 用于接收基于推送的无值完成或错误通知.
 *
 * <p>当通过{@link CompletableSource＃subscribe(CompletableObserver)}方法将{@code CompletableObserver}
 * 订阅到{@link CompletableSource}时, {@code CompletableSource}会调用{@link #onSubscribe(Disposable)}
 * {@link Disposable}允许随时处理序列. 行为良好的{@code CompletableSource}会调用一次{@code CompletableObserver}的{@link
 * #onError(Throwable)} 或{@link #onComplete()}方法一次, 因为它们被认为是互斥的<strong>终端信号</strong>.
 *
 * <p>调用{@code CompletableObserver}的方法必须以序列化的方式进行, 也就是说, 一定不能由多个线程以重叠的方式同时调用它们, 并且调用模式必须遵循以下协议:
 *
 * <pre><code>    onSubscribe (onError | onComplete)?</code></pre>
 *
 * <p>不建议您将{@code CompletableObserver}订阅到多个{@code CompletableSource}. 如果发生此类重用, 则{@code
 * CompletableObserver} 实现的职责是准备接收对其方法的多次调用, 并确保其业务逻辑正确并发.
 *
 * <p>使用{@code null} 参数禁止调用{@link #onSubscribe(Disposable)}或{@link #onError(Throwable)}.
 *
 * <p>{@code onXXX}方法的实现应避免引发运行时异常, 下列情况除外:
 *
 * <ul>
 *   <li>如果参数为{@code null}, 则方法可以引发{@code NullPointerException}. 注意尽管RxJava阻止{@code null}进入流,
 *       因此通常不需要检查从标准源和中间运算符汇编而来的流中是否有null.
 *   <li>如果出现致命错误(例如{@code VirtualMachineError}).
 * </ul>
 *
 * @since 2.0
 */
public interface CompletableObserver {
  /**
   * 由{@link Completable}调用一次以在此实例上设置{@link Disposable}, 然后可随时用来取消订阅.
   *
   * @param d {@code Disposable}实例以调用dispose进行取消, 而不是null
   */
  void onSubscribe(@NonNull Disposable d);

  /** 延迟计算正常完成后调用. */
  void onComplete();

  /**
   * 如果延迟的计算“引发”异常, 则调用一次.
   *
   * @param e 异常, 不是{@code null}.
   */
  void onError(@NonNull Throwable e);
}
