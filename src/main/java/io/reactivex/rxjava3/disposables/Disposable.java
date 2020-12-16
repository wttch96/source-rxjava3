package io.reactivex.rxjava3.disposables;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.internal.disposables.EmptyDisposable;
import io.reactivex.rxjava3.internal.functions.Functions;
import java.util.Objects;
import java.util.concurrent.Future;
import org.reactivestreams.Subscription;

/** 代表一个一次性的资源. */
public interface Disposable {
  /** 处置资源, 操作应该是幂等的. */
  void dispose();

  /**
   * 如果已处理此资源, 则返回true.
   *
   * @return true if this resource has been disposed
   */
  boolean isDisposed();

  /**
   * 通过包装{@link Runnable}构造一个{@code Disposable}, 该{@link Runnable}在处理{@code Disposable}时仅执行一次.
   *
   * @param run 要包装的 Runnable
   * @return 新的Disposable实例
   * @throws NullPointerException 如果{@code run}为{@code null}
   * @since 3.0.0
   */
  @NonNull
  static Disposable fromRunnable(@NonNull Runnable run) {
    Objects.requireNonNull(run, "run is null");
    return new RunnableDisposable(run);
  }

  /**
   * 通过包装一个{@link Action}来构造一个{@code Disposable}, 该{@link Action}在处理完{@code Disposable}后将被执行一次.
   *
   * @param action the Action to wrap
   * @return the new Disposable instance
   * @throws NullPointerException if {@code action} is {@code null}
   * @since 3.0.0
   */
  @NonNull
  static Disposable fromAction(@NonNull Action action) {
    Objects.requireNonNull(action, "action is null");
    return new ActionDisposable(action);
  }

  /**
   * Construct a {@code Disposable} by wrapping a {@link Future} that is cancelled exactly once when
   * the {@code Disposable} is disposed.
   *
   * <p>The {@code Future} is cancelled with {@code mayInterruptIfRunning == true}.
   *
   * @param future the Future to wrap
   * @return the new Disposable instance
   * @throws NullPointerException if {@code future} is {@code null}
   * @see #fromFuture(Future, boolean)
   * @since 3.0.0
   */
  @NonNull
  static Disposable fromFuture(@NonNull Future<?> future) {
    Objects.requireNonNull(future, "future is null");
    return fromFuture(future, true);
  }

  /**
   * 通过包装一个{@link Future}构造一个{@code Disposable}, 该{@link Future}在处理{@code Disposable}时会被完全取消一次.
   *
   * @param future the Future to wrap
   * @param allowInterrupt if true, the future cancel happens via {@code Future.cancel(true)}
   * @return the new Disposable instance
   * @throws NullPointerException if {@code future} is {@code null}
   * @since 3.0.0
   */
  @NonNull
  static Disposable fromFuture(@NonNull Future<?> future, boolean allowInterrupt) {
    Objects.requireNonNull(future, "future is null");
    return new FutureDisposable(future, allowInterrupt);
  }

  /**
   * 通过包装{@link Subscription}, 构造一个{@code Disposable}, 当处置{@code Disposable}时, 该{@link
   * Subscription}将被完全取消一次.
   *
   * @param subscription the Runnable to wrap
   * @return the new Disposable instance
   * @throws NullPointerException if {@code subscription} is {@code null}
   * @since 3.0.0
   */
  @NonNull
  static Disposable fromSubscription(@NonNull Subscription subscription) {
    Objects.requireNonNull(subscription, "subscription is null");
    return new SubscriptionDisposable(subscription);
  }

  /**
   * 通过包装一个{@link AutoCloseable}构造一个{@code Disposable}, 该{@link AutoCloseable}在处理{@code
   * Disposable}时会被完全关闭一次.
   *
   * @param autoCloseable the AutoCloseable to wrap
   * @return the new Disposable instance
   * @throws NullPointerException if {@code autoCloseable} is {@code null}
   * @since 3.0.0
   */
  @NonNull
  static Disposable fromAutoCloseable(@NonNull AutoCloseable autoCloseable) {
    Objects.requireNonNull(autoCloseable, "autoCloseable is null");
    return new AutoCloseableDisposable(autoCloseable);
  }

  /**
   * 通过包装{@code Disposable}构造一个{@link AutoCloseable}, 该{@code Disposable}在返回的{@code AutoCloseable}关闭时被处置.
   *
   * @param disposable the Disposable instance
   * @return the new AutoCloseable instance
   * @throws NullPointerException if {@code disposable} is {@code null}
   * @since 3.0.0
   */
  @NonNull
  static AutoCloseable toAutoCloseable(@NonNull Disposable disposable) {
    Objects.requireNonNull(disposable, "disposable is null");
    return disposable::dispose;
  }

  /**
   * 返回一个新的未处置的{@code Disposable}实例.
   *
   * @return 一个新的未处置的{@code Disposable}实例
   * @since 3.0.0
   */
  @NonNull
  static Disposable empty() {
    return fromRunnable(Functions.EMPTY_RUNNABLE);
  }

  /**
   * 返回一个共享的, 已处置的{@code Disposable}实例.
   *
   * @return 一个共享的, 已处置的{@code Disposable}实例
   * @since 3.0.0
   */
  @NonNull
  static Disposable disposed() {
    return EmptyDisposable.INSTANCE;
  }
}
