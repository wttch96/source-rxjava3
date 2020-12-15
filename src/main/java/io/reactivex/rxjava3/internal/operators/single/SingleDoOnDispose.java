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
package io.reactivex.rxjava3.internal.operators.single;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.exceptions.Exceptions;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.internal.disposables.DisposableHelper;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleDoOnDispose<T> extends Single<T> {
  final SingleSource<T> source;

  final Action onDispose;

  public SingleDoOnDispose(SingleSource<T> source, Action onDispose) {
    this.source = source;
    this.onDispose = onDispose;
  }

  @Override
  protected void subscribeActual(final SingleObserver<? super T> observer) {

    source.subscribe(new DoOnDisposeObserver<>(observer, onDispose));
  }

  static final class DoOnDisposeObserver<T> extends AtomicReference<Action>
      implements SingleObserver<T>, Disposable {
    private static final long serialVersionUID = -8583764624474935784L;

    final SingleObserver<? super T> downstream;

    Disposable upstream;

    DoOnDisposeObserver(SingleObserver<? super T> actual, Action onDispose) {
      this.downstream = actual;
      this.lazySet(onDispose);
    }

    @Override
    public void dispose() {
      Action a = getAndSet(null);
      if (a != null) {
        try {
          a.run();
        } catch (Throwable ex) {
          Exceptions.throwIfFatal(ex);
          RxJavaPlugins.onError(ex);
        }
        upstream.dispose();
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

    @Override
    public void onSuccess(T value) {
      downstream.onSuccess(value);
    }

    @Override
    public void onError(Throwable e) {
      downstream.onError(e);
    }
  }
}
