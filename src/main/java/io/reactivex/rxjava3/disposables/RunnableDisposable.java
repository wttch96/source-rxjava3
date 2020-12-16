package io.reactivex.rxjava3.disposables;

import io.reactivex.rxjava3.annotations.NonNull;

/** 管理{@link Runnable}实例的一次性容器. */
final class RunnableDisposable extends ReferenceDisposable<Runnable> {

  private static final long serialVersionUID = -8219729196779211169L;

  RunnableDisposable(Runnable value) {
    super(value);
  }

  @Override
  protected void onDisposed(@NonNull Runnable value) {
    value.run();
  }

  @Override
  public String toString() {
    return "RunnableDisposable(disposed=" + isDisposed() + ", " + get() + ")";
  }
}
