package io.reactivex.rxjava3.disposables;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.internal.util.ExceptionHelper;

/** 管理{@link Action}实例的一次性容器. */
final class ActionDisposable extends ReferenceDisposable<Action> {

  private static final long serialVersionUID = -8219729196779211169L;

  ActionDisposable(Action value) {
    super(value);
  }

  @Override
  protected void onDisposed(@NonNull Action value) {
    try {
      value.run();
    } catch (Throwable ex) {
      throw ExceptionHelper.wrapOrThrow(ex);
    }
  }

  @Override
  public String toString() {
    return "ActionDisposable(disposed=" + isDisposed() + ", " + get() + ")";
  }
}
