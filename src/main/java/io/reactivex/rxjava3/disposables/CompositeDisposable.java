package io.reactivex.rxjava3.disposables;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.exceptions.CompositeException;
import io.reactivex.rxjava3.exceptions.Exceptions;
import io.reactivex.rxjava3.internal.util.ExceptionHelper;
import io.reactivex.rxjava3.internal.util.OpenHashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 可以容纳多个其他{@link Disposable}并为{@link #add(Disposable)}, {@link #remove(Disposable)}
 * 提供<em>O(1)</em>时间复杂度的一次性容器}和{@link #delete(Disposable)}操作.
 */
public final class CompositeDisposable implements Disposable, DisposableContainer {

  OpenHashSet<Disposable> resources;

  volatile boolean disposed;

  /** 创建一个空的{@code CompositeDisposable}. */
  public CompositeDisposable() {}

  /**
   * 使用给定的初始{@link Disposable}元素数组创建一个{@code CompositeDisposable}.
   *
   * @param disposables 以{@code Disposable}开头的数组
   * @throws NullPointerException 如果{@code disposables}或其任何数组项目为{@code null}
   */
  public CompositeDisposable(@NonNull Disposable... disposables) {
    Objects.requireNonNull(disposables, "disposables is null");
    this.resources = new OpenHashSet<>(disposables.length + 1);
    for (Disposable d : disposables) {
      Objects.requireNonNull(d, "A Disposable in the disposables array is null");
      this.resources.add(d);
    }
  }

  /**
   * 使用给定的初始{@link Disposable}元素的{@link Iterable}序列创建一个{@code CompositeDisposable}.
   *
   * @param disposables {@code Disposable}的{@code Iterable}序列
   * @throws NullPointerException 如果{@code Disposable}或其任何物品为{@code null}
   */
  public CompositeDisposable(@NonNull Iterable<? extends Disposable> disposables) {
    Objects.requireNonNull(disposables, "disposables is null");
    this.resources = new OpenHashSet<>();
    for (Disposable d : disposables) {
      Objects.requireNonNull(d, "A Disposable item in the disposables sequence is null");
      this.resources.add(d);
    }
  }

  @Override
  public void dispose() {
    if (disposed) {
      return;
    }
    OpenHashSet<Disposable> set;
    synchronized (this) {
      if (disposed) {
        return;
      }
      disposed = true;
      set = resources;
      resources = null;
    }

    dispose(set);
  }

  @Override
  public boolean isDisposed() {
    return disposed;
  }

  /**
   * *将{@link Disposable}添加到此容器中, 如果容器已处理, 则将其处置.
   *
   * @param disposable 要添加的{@code Disposable}, 而不是{@code null}
   * @return 如果成功, 则为{@code true}, 如果已处置此容器, 则为{@code false}
   * @throws NullPointerException 如果{@code disposable}为{@code null}
   */
  @Override
  public boolean add(@NonNull Disposable disposable) {
    Objects.requireNonNull(disposable, "disposable is null");
    if (!disposed) {
      synchronized (this) {
        if (!disposed) {
          OpenHashSet<Disposable> set = resources;
          if (set == null) {
            set = new OpenHashSet<>();
            resources = set;
          }
          set.add(disposable);
          return true;
        }
      }
    }
    disposable.dispose();
    return false;
  }

  /**
   * 以原子方式将给定的{@link Disposable}数组添加到容器中, 或者如果已丢弃容器, 则将它们全部处置.
   *
   * @param disposables {@code Disposable}的数组
   * @return {@code true}如果操作成功, {@code false}如果容器已处置
   * @throws NullPointerException 如果{@code Disposable}或其任何数组项目为{@code null}
   */
  public boolean addAll(@NonNull Disposable... disposables) {
    Objects.requireNonNull(disposables, "disposables is null");
    if (!disposed) {
      synchronized (this) {
        if (!disposed) {
          OpenHashSet<Disposable> set = resources;
          if (set == null) {
            set = new OpenHashSet<>(disposables.length + 1);
            resources = set;
          }
          for (Disposable d : disposables) {
            Objects.requireNonNull(d, "A Disposable in the disposables array is null");
            set.add(d);
          }
          return true;
        }
      }
    }
    for (Disposable d : disposables) {
      d.dispose();
    }
    return false;
  }

  /**
   * 移除并处置给定的{@link Disposable}(如果它是此容器的一部分).
   *
   * @param disposable 一次性物品, 以去除和处置, 而不是{@code null}
   * @return {@code true}如果操作成功
   * @throws NullPointerException 如果{@code disposable}为{@code null}
   */
  @Override
  public boolean remove(@NonNull Disposable disposable) {
    if (delete(disposable)) {
      disposable.dispose();
      return true;
    }
    return false;
  }

  /**
   * 如果给定的{@link Disposable}是此容器的一部分, 则删除(但不处置).
   *
   * @param disposable 一次性物品要移除, 而不是{@code null}
   * @return {@code true}如果操作成功
   * @throws NullPointerException 如果{@code disposable}为{@code null}
   */
  @Override
  public boolean delete(@NonNull Disposable disposable) {
    Objects.requireNonNull(disposable, "disposable is null");
    if (disposed) {
      return false;
    }
    synchronized (this) {
      if (disposed) {
        return false;
      }

      OpenHashSet<Disposable> set = resources;
      if (set == null || !set.remove(disposable)) {
        return false;
      }
    }
    return true;
  }

  /** 以原子方式清除容器, 然后处置所有先前包含的{@link Disposable}. */
  public void clear() {
    if (disposed) {
      return;
    }
    OpenHashSet<Disposable> set;
    synchronized (this) {
      if (disposed) {
        return;
      }

      set = resources;
      resources = null;
    }

    dispose(set);
  }

  /**
   * 返回当前持有的{@link Disposable}的数量.
   *
   * @return 当前持有的{@link Disposable}的数量
   */
  public int size() {
    if (disposed) {
      return 0;
    }
    synchronized (this) {
      if (disposed) {
        return 0;
      }
      OpenHashSet<Disposable> set = resources;
      return set != null ? set.size() : 0;
    }
  }

  /**
   * 通过抑制非致命的{@link Throwable}直到最后, 处置{@link OpenHashSet}的内容.
   *
   * @param set 要处置的 {@code OpenHashSet}
   */
  void dispose(@Nullable OpenHashSet<Disposable> set) {
    if (set == null) {
      return;
    }
    List<Throwable> errors = null;
    Object[] array = set.keys();
    for (Object o : array) {
      if (o instanceof Disposable) {
        try {
          ((Disposable) o).dispose();
        } catch (Throwable ex) {
          Exceptions.throwIfFatal(ex);
          if (errors == null) {
            errors = new ArrayList<>();
          }
          errors.add(ex);
        }
      }
    }
    if (errors != null) {
      if (errors.size() == 1) {
        throw ExceptionHelper.wrapOrThrow(errors.get(0));
      }
      throw new CompositeException(errors);
    }
  }
}
