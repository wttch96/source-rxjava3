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
import io.reactivex.rxjava3.functions.Supplier;
import io.reactivex.rxjava3.internal.util.ExceptionHelper;

/**
 * Signals a Throwable returned by a Supplier.
 *
 * @param <T> the value type
 */
public final class MaybeErrorCallable<T> extends Maybe<T> {

  final Supplier<? extends Throwable> errorSupplier;

  public MaybeErrorCallable(Supplier<? extends Throwable> errorSupplier) {
    this.errorSupplier = errorSupplier;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
    observer.onSubscribe(Disposable.disposed());
    Throwable ex;

    try {
      ex = ExceptionHelper.nullCheck(errorSupplier.get(), "Supplier returned a null Throwable.");
    } catch (Throwable ex1) {
      Exceptions.throwIfFatal(ex1);
      ex = ex1;
    }

    observer.onError(ex);
  }
}
