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
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.internal.disposables.DisposableHelper;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class CompletableDelay extends Completable {

  final CompletableSource source;

  final long delay;

  final TimeUnit unit;

  final Scheduler scheduler;

  final boolean delayError;

  public CompletableDelay(
      CompletableSource source,
      long delay,
      TimeUnit unit,
      Scheduler scheduler,
      boolean delayError) {
    this.source = source;
    this.delay = delay;
    this.unit = unit;
    this.scheduler = scheduler;
    this.delayError = delayError;
  }

  @Override
  protected void subscribeActual(final CompletableObserver observer) {
    source.subscribe(new Delay(observer, delay, unit, scheduler, delayError));
  }

  static final class Delay extends AtomicReference<Disposable>
      implements CompletableObserver, Runnable, Disposable {

    private static final long serialVersionUID = 465972761105851022L;

    final CompletableObserver downstream;

    final long delay;

    final TimeUnit unit;

    final Scheduler scheduler;

    final boolean delayError;

    Throwable error;

    Delay(
        CompletableObserver downstream,
        long delay,
        TimeUnit unit,
        Scheduler scheduler,
        boolean delayError) {
      this.downstream = downstream;
      this.delay = delay;
      this.unit = unit;
      this.scheduler = scheduler;
      this.delayError = delayError;
    }

    @Override
    public void onSubscribe(Disposable d) {
      if (DisposableHelper.setOnce(this, d)) {
        downstream.onSubscribe(this);
      }
    }

    @Override
    public void onComplete() {
      DisposableHelper.replace(this, scheduler.scheduleDirect(this, delay, unit));
    }

    @Override
    public void onError(final Throwable e) {
      error = e;
      DisposableHelper.replace(this, scheduler.scheduleDirect(this, delayError ? delay : 0, unit));
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
    public void run() {
      Throwable e = error;
      error = null;
      if (e != null) {
        downstream.onError(e);
      } else {
        downstream.onComplete();
      }
    }
  }
}
