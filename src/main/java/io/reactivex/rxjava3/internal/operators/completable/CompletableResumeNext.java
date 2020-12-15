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
package io.reactivex.rxjava3.internal.operators.completable;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.CompletableSource;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.exceptions.CompositeException;
import io.reactivex.rxjava3.exceptions.Exceptions;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.internal.disposables.DisposableHelper;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public final class CompletableResumeNext extends Completable {

  final CompletableSource source;

  final Function<? super Throwable, ? extends CompletableSource> errorMapper;

  public CompletableResumeNext(
      CompletableSource source,
      Function<? super Throwable, ? extends CompletableSource> errorMapper) {
    this.source = source;
    this.errorMapper = errorMapper;
  }

  @Override
  protected void subscribeActual(final CompletableObserver observer) {
    ResumeNextObserver parent = new ResumeNextObserver(observer, errorMapper);
    observer.onSubscribe(parent);
    source.subscribe(parent);
  }

  static final class ResumeNextObserver extends AtomicReference<Disposable>
      implements CompletableObserver, Disposable {

    private static final long serialVersionUID = 5018523762564524046L;

    final CompletableObserver downstream;

    final Function<? super Throwable, ? extends CompletableSource> errorMapper;

    boolean once;

    ResumeNextObserver(
        CompletableObserver observer,
        Function<? super Throwable, ? extends CompletableSource> errorMapper) {
      this.downstream = observer;
      this.errorMapper = errorMapper;
    }

    @Override
    public void onSubscribe(Disposable d) {
      DisposableHelper.replace(this, d);
    }

    @Override
    public void onComplete() {
      downstream.onComplete();
    }

    @Override
    public void onError(Throwable e) {
      if (once) {
        downstream.onError(e);
        return;
      }
      once = true;

      CompletableSource c;

      try {
        c =
            Objects.requireNonNull(
                errorMapper.apply(e), "The errorMapper returned a null CompletableSource");
      } catch (Throwable ex) {
        Exceptions.throwIfFatal(ex);
        downstream.onError(new CompositeException(e, ex));
        return;
      }

      c.subscribe(this);
    }

    @Override
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }

    @Override
    public void dispose() {
      DisposableHelper.dispose(this);
    }
  }
}
