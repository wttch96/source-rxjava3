/**
 * Copyright (c) 2016-present, RxJava Contributors.
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.reactivex.rxjava3.internal.operators.maybe;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.core.MaybeSource;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.exceptions.CompositeException;
import io.reactivex.rxjava3.exceptions.Exceptions;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Supplier;
import io.reactivex.rxjava3.internal.disposables.DisposableHelper;
import io.reactivex.rxjava3.internal.disposables.EmptyDisposable;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Creates a resource and a dependent Maybe for each incoming Observer and optionally disposes the
 * resource eagerly (before the terminal event is send out).
 *
 * @param <T> the value type
 * @param <D> the resource type
 */
public final class MaybeUsing<T, D> extends Maybe<T> {

  final Supplier<? extends D> resourceSupplier;

  final Function<? super D, ? extends MaybeSource<? extends T>> sourceSupplier;

  final Consumer<? super D> resourceDisposer;

  final boolean eager;

  public MaybeUsing(
      Supplier<? extends D> resourceSupplier,
      Function<? super D, ? extends MaybeSource<? extends T>> sourceSupplier,
      Consumer<? super D> resourceDisposer,
      boolean eager) {
    this.resourceSupplier = resourceSupplier;
    this.sourceSupplier = sourceSupplier;
    this.resourceDisposer = resourceDisposer;
    this.eager = eager;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
    D resource;

    try {
      resource = resourceSupplier.get();
    } catch (Throwable ex) {
      Exceptions.throwIfFatal(ex);
      EmptyDisposable.error(ex, observer);
      return;
    }

    MaybeSource<? extends T> source;

    try {
      source =
          Objects.requireNonNull(
              sourceSupplier.apply(resource), "The sourceSupplier returned a null MaybeSource");
    } catch (Throwable ex) {
      Exceptions.throwIfFatal(ex);
      if (eager) {
        try {
          resourceDisposer.accept(resource);
        } catch (Throwable exc) {
          Exceptions.throwIfFatal(exc);
          EmptyDisposable.error(new CompositeException(ex, exc), observer);
          return;
        }
      }

      EmptyDisposable.error(ex, observer);

      if (!eager) {
        try {
          resourceDisposer.accept(resource);
        } catch (Throwable exc) {
          Exceptions.throwIfFatal(exc);
          RxJavaPlugins.onError(exc);
        }
      }
      return;
    }

    source.subscribe(new UsingObserver<T, D>(observer, resource, resourceDisposer, eager));
  }

  static final class UsingObserver<T, D> extends AtomicReference<Object>
      implements MaybeObserver<T>, Disposable {

    private static final long serialVersionUID = -674404550052917487L;

    final MaybeObserver<? super T> downstream;

    final Consumer<? super D> disposer;

    final boolean eager;

    Disposable upstream;

    UsingObserver(
        MaybeObserver<? super T> actual, D resource, Consumer<? super D> disposer, boolean eager) {
      super(resource);
      this.downstream = actual;
      this.disposer = disposer;
      this.eager = eager;
    }

    @Override
    public void dispose() {
      if (eager) {
        disposeResource();
        upstream.dispose();
        upstream = DisposableHelper.DISPOSED;
      } else {
        upstream.dispose();
        upstream = DisposableHelper.DISPOSED;
        disposeResource();
      }
    }

    @SuppressWarnings("unchecked")
    void disposeResource() {
      Object resource = getAndSet(this);
      if (resource != this) {
        try {
          disposer.accept((D) resource);
        } catch (Throwable ex) {
          Exceptions.throwIfFatal(ex);
          RxJavaPlugins.onError(ex);
        }
      }
    }

    @Override
    public boolean isDisposed() {
      return upstream.isDisposed();
    }

    @Override
    public void onSubscribe(Disposable d) {
      if (DisposableHelper.validate(this.upstream, d)) {
        this.upstream = d;

        downstream.onSubscribe(this);
      }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onSuccess(T value) {
      upstream = DisposableHelper.DISPOSED;
      if (eager) {
        Object resource = getAndSet(this);
        if (resource != this) {
          try {
            disposer.accept((D) resource);
          } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            downstream.onError(ex);
            return;
          }
        } else {
          return;
        }
      }

      downstream.onSuccess(value);

      if (!eager) {
        disposeResource();
      }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onError(Throwable e) {
      upstream = DisposableHelper.DISPOSED;
      if (eager) {
        Object resource = getAndSet(this);
        if (resource != this) {
          try {
            disposer.accept((D) resource);
          } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            e = new CompositeException(e, ex);
          }
        } else {
          return;
        }
      }

      downstream.onError(e);

      if (!eager) {
        disposeResource();
      }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onComplete() {
      upstream = DisposableHelper.DISPOSED;
      if (eager) {
        Object resource = getAndSet(this);
        if (resource != this) {
          try {
            disposer.accept((D) resource);
          } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            downstream.onError(ex);
            return;
          }
        } else {
          return;
        }
      }

      downstream.onComplete();

      if (!eager) {
        disposeResource();
      }
    }
  }
}
