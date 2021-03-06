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

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.CompletableSource;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.core.MaybeSource;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.exceptions.Exceptions;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.internal.disposables.DisposableHelper;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Maps the success value of the source MaybeSource into a Completable.
 *
 * @param <T> the value type of the source MaybeSource
 */
public final class MaybeFlatMapCompletable<T> extends Completable {

  final MaybeSource<T> source;

  final Function<? super T, ? extends CompletableSource> mapper;

  public MaybeFlatMapCompletable(
      MaybeSource<T> source, Function<? super T, ? extends CompletableSource> mapper) {
    this.source = source;
    this.mapper = mapper;
  }

  @Override
  protected void subscribeActual(CompletableObserver observer) {
    FlatMapCompletableObserver<T> parent = new FlatMapCompletableObserver<>(observer, mapper);
    observer.onSubscribe(parent);
    source.subscribe(parent);
  }

  static final class FlatMapCompletableObserver<T> extends AtomicReference<Disposable>
      implements MaybeObserver<T>, CompletableObserver, Disposable {

    private static final long serialVersionUID = -2177128922851101253L;

    final CompletableObserver downstream;

    final Function<? super T, ? extends CompletableSource> mapper;

    FlatMapCompletableObserver(
        CompletableObserver actual, Function<? super T, ? extends CompletableSource> mapper) {
      this.downstream = actual;
      this.mapper = mapper;
    }

    @Override
    public void dispose() {
      DisposableHelper.dispose(this);
    }

    @Override
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }

    @Override
    public void onSubscribe(Disposable d) {
      DisposableHelper.replace(this, d);
    }

    @Override
    public void onSuccess(T value) {
      CompletableSource cs;

      try {
        cs =
            Objects.requireNonNull(
                mapper.apply(value), "The mapper returned a null CompletableSource");
      } catch (Throwable ex) {
        Exceptions.throwIfFatal(ex);
        onError(ex);
        return;
      }

      if (!isDisposed()) {
        cs.subscribe(this);
      }
    }

    @Override
    public void onError(Throwable e) {
      downstream.onError(e);
    }

    @Override
    public void onComplete() {
      downstream.onComplete();
    }
  }
}
