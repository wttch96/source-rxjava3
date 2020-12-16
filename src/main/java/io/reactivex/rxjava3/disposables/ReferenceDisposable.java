package io.reactivex.rxjava3.disposables;

import io.reactivex.rxjava3.annotations.NonNull;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 一次性容器的基类, 可管理在处置容器时必须运行的其他某种类型.
 *
 * @param <T> 包含的类型
 */
abstract class ReferenceDisposable<T> extends AtomicReference<T> implements Disposable {

  private static final long serialVersionUID = 6537757548749041217L;

  ReferenceDisposable(T value) {
    super(Objects.requireNonNull(value, "value is null"));
  }

  /** 处置容器内的元素 */
  protected abstract void onDisposed(@NonNull T value);

  @Override
  public final void dispose() {
    T value = get();
    if (value != null) {
      value = getAndSet(null);
      if (value != null) {
        onDisposed(value);
      }
    }
  }

  @Override
  public final boolean isDisposed() {
    return get() == null;
  }
}
