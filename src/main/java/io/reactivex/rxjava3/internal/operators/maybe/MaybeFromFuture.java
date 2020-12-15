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
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.exceptions.Exceptions;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Waits until the source Future completes or the wait times out; treats a {@code null} result as
 * indication to signal {@code onComplete} instead of {@code onSuccess}.
 *
 * @param <T> the value type
 */
public final class MaybeFromFuture<T> extends Maybe<T> {

  final Future<? extends T> future;

  final long timeout;

  final TimeUnit unit;

  public MaybeFromFuture(Future<? extends T> future, long timeout, TimeUnit unit) {
    this.future = future;
    this.timeout = timeout;
    this.unit = unit;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
    Disposable d = Disposable.empty();
    observer.onSubscribe(d);
    if (!d.isDisposed()) {
      T v;
      try {
        if (timeout <= 0L) {
          v = future.get();
        } else {
          v = future.get(timeout, unit);
        }
      } catch (Throwable ex) {
        Exceptions.throwIfFatal(ex);
        if (ex instanceof ExecutionException) {
          ex = ex.getCause();
        }
        Exceptions.throwIfFatal(ex);
        if (!d.isDisposed()) {
          observer.onError(ex);
        }
        return;
      }
      if (!d.isDisposed()) {
        if (v == null) {
          observer.onComplete();
        } else {
          observer.onSuccess(v);
        }
      }
    }
  }
}
