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
package io.reactivex.rxjava3.internal.observers;

import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.exceptions.CompositeException;
import io.reactivex.rxjava3.exceptions.Exceptions;
import io.reactivex.rxjava3.functions.BiConsumer;
import io.reactivex.rxjava3.internal.disposables.DisposableHelper;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicReference;

public final class BiConsumerSingleObserver<T> extends AtomicReference<Disposable>
    implements SingleObserver<T>, Disposable {

  private static final long serialVersionUID = 4943102778943297569L;
  final BiConsumer<? super T, ? super Throwable> onCallback;

  public BiConsumerSingleObserver(BiConsumer<? super T, ? super Throwable> onCallback) {
    this.onCallback = onCallback;
  }

  @Override
  public void onError(Throwable e) {
    try {
      lazySet(DisposableHelper.DISPOSED);
      onCallback.accept(null, e);
    } catch (Throwable ex) {
      Exceptions.throwIfFatal(ex);
      RxJavaPlugins.onError(new CompositeException(e, ex));
    }
  }

  @Override
  public void onSubscribe(Disposable d) {
    DisposableHelper.setOnce(this, d);
  }

  @Override
  public void onSuccess(T value) {
    try {
      lazySet(DisposableHelper.DISPOSED);
      onCallback.accept(value, null);
    } catch (Throwable ex) {
      Exceptions.throwIfFatal(ex);
      RxJavaPlugins.onError(ex);
    }
  }

  @Override
  public void dispose() {
    DisposableHelper.dispose(this);
  }

  @Override
  public boolean isDisposed() {
    return get() == DisposableHelper.DISPOSED;
  }
}
