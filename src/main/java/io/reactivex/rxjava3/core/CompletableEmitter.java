package io.reactivex.rxjava3.core;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Cancellable;

/**
 * 通过RxJava {@link CompletableObserver}进行抽象, 该抽象允许将资源与其关联.
 *
 * <p>所有方法都可以从多个线程安全地调用, 但是请注意, 不能保证其*终端事件将获胜并传递给下游.
 *
 * <p>多次调用{@link #onComplete()}无效. 多次或在{@code onComplete}之后调用{@link #onError(Throwable)} 将通过{@link
 * io.reactivex.rxjava3.plugins.RxJavaPlugins#onError(Throwable)}将异常路由到全局错误处理程序中.
 *
 * <p>T发射器允许分别通过{@link #setDisposable(Disposable)}或{@link #setCancellable(Cancellable)} 以{@link
 * Disposable} 或{@link Cancellable}的形式注册单个资源. 当下游取消流或事件生成器逻辑调用{@link #onError(Throwable)}, {@link
 * #onComplete()}或{@link #tryOnError(Throwable)}成功.
 *
 * <p>一次只能将一个{@code Disposable}或{@code Cancellable}对象与发射器关联. 调用任一{@code set}方法将处理/取消任何先前的对象.
 * 如果需要处理多种资源, 则可以创建{@link io.reactivex.rxjava3.disposables.CompositeDisposable}并将其与发射器关联.
 *
 * <p>{@link Cancellable}在逻辑上等效于{@code Disposable}, 但允许使用可以引发已检查异常的清理逻辑(例如Java IO 组件上的许多{@code
 * close()}方法). 由于资源释放是在终端事件传递完成或序列被取消后发生的, 因此{@code Cancellable}中引发的异常将通过{@link
 * io.reactivex.rxjava3.plugins.RxJavaPlugins#onError(Throwable)}.
 */
public interface CompletableEmitter {

  /** 完成的信号. */
  void onComplete();

  /**
   * 异常信号.
   *
   * @param t the exception, not null
   */
  void onError(@NonNull Throwable t);

  /**
   * 在此发射器上设置一个Disposable; 任何先前的{@link Disposable}或{@link Cancellable}将被处置/取消.
   *
   * @param d 一次性的, 允许为空
   */
  void setDisposable(@Nullable Disposable d);

  /**
   * 在此发射器上设置一个Cancelable; 任何先前的{@link Disposable}或{@link Cancellable}将被处置/取消.
   *
   * @param c 可取消资源, 允许为null
   */
  void setCancellable(@Nullable Cancellable c);

  /**
   * 如果下游处理序列或发射器通过{@link #onError(Throwable)}, {@link #onComplete}或成功的{@link
   * #tryOnError(Throwable)} 终止, 则返回true.
   *
   * <p>此方法是线程安全的.
   *
   * @return true 如果下游处理了序列或发射器被终止
   */
  boolean isDisposed();

  /**
   * 如果下游没有取消序列或以其他方式终止, 则尝试发出指定的{@link Throwable}错误, 如果由于生命周期的限制, 如果不允许发生，则返回false.
   *
   * <p>与{@link #onError(Throwable)}不同, 如果无法传递错误, 则不会调用{@link
   * io.reactivex.rxjava3.plugins.RxJavaPlugins#onError(Throwable) RxjavaPlugins.onError}.
   *
   * <p>History: 2.1.1 - experimental
   *
   * @param t 如果可能的话抛出的错误
   * @return true 如果成功, 则为false, 如果下游无法接受其他事件
   * @since 2.2
   */
  boolean tryOnError(@NonNull Throwable t);
}
