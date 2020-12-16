package io.reactivex.rxjava3.disposables;

import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicReference;

/** 一个Disposable容器, 它可以用另一个Disposable原子地更新/替换所包含的Disposable, 在更新时处置旧容器, 并在处置容器本身时处理该处置. */
public final class SerialDisposable implements Disposable {
  final AtomicReference<Disposable> resource;

  /** 构造一个空的SerialDisposable. */
  public SerialDisposable() {
    this.resource = new AtomicReference<>();
  }

  /**
   * 用给定的初始Disposable实例构造一个SerialDisposable.
   *
   * @param initialDisposable 要使用的初始Disposable实例, 允许为null
   */
  public SerialDisposable(@Nullable Disposable initialDisposable) {
    this.resource = new AtomicReference<>(initialDisposable);
  }

  /**
   * 原子地: 将下一个一次性用品放在此容器上, 并处置前一个(如果有的话); 如果已处置该容器, 则处置下一个.
   *
   * @param next the Disposable to set, may be null
   * @return true if the operation succeeded, false if the container has been disposed
   * @see #replace(Disposable)
   */
  public boolean set(@Nullable Disposable next) {
    return DisposableHelper.set(resource, next);
  }

  /**
   * 原子地: 将下一个一次性容器放在此容器上, 但不要丢弃上一个(如果有的话)或如果容器已经处置了则丢弃下一个.
   *
   * @param next the Disposable to set, may be null
   * @return true if the operation succeeded, false if the container has been disposed
   * @see #set(Disposable)
   */
  public boolean replace(@Nullable Disposable next) {
    return DisposableHelper.replace(resource, next);
  }

  /**
   * 返回当前包含的Disposable; 如果此容器为空, 则返回null.
   *
   * @return 当前的Disposable, 可以为null
   */
  @Nullable
  public Disposable get() {
    Disposable d = resource.get();
    if (d == DisposableHelper.DISPOSED) {
      return Disposable.disposed();
    }
    return d;
  }

  @Override
  public void dispose() {
    DisposableHelper.dispose(resource);
  }

  @Override
  public boolean isDisposed() {
    return DisposableHelper.isDisposed(resource.get());
  }
}
