package io.reactivex.rxjava3.disposables;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

/** 取消{@link Future}实例的一次性容器. */
final class FutureDisposable extends AtomicReference<Future<?>> implements Disposable {

  private static final long serialVersionUID = 6545242830671168775L;

  private final boolean allowInterrupt;

  FutureDisposable(Future<?> run, boolean allowInterrupt) {
    super(run);
    this.allowInterrupt = allowInterrupt;
  }

  @Override
  public boolean isDisposed() {
    Future<?> f = get();
    return f == null || f.isDone();
  }

  @Override
  public void dispose() {
    Future<?> f = getAndSet(null);
    if (f != null) {
      f.cancel(allowInterrupt);
    }
  }
}
