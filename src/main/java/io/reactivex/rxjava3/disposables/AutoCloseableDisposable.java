package io.reactivex.rxjava3.disposables;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.internal.util.ExceptionHelper;

/**
 * 管理{@link AutoCloseable}实例的一次性容器.
 *
 * @since 3.0.0
 */
final class AutoCloseableDisposable extends ReferenceDisposable<AutoCloseable> {

  private static final long serialVersionUID = -6646144244598696847L;

  AutoCloseableDisposable(AutoCloseable value) {
    super(value);
  }

  @Override
  protected void onDisposed(@NonNull AutoCloseable value) {
    try {
      value.close();
    } catch (Throwable ex) {
      throw ExceptionHelper.wrapOrThrow(ex);
    }
  }

  @Override
  public String toString() {
    return "AutoCloseableDisposable(disposed=" + isDisposed() + ", " + get() + ")";
  }
}
