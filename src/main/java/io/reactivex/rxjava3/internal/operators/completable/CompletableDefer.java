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
import io.reactivex.rxjava3.exceptions.Exceptions;
import io.reactivex.rxjava3.functions.Supplier;
import io.reactivex.rxjava3.internal.disposables.EmptyDisposable;
import java.util.Objects;

public final class CompletableDefer extends Completable {

  final Supplier<? extends CompletableSource> completableSupplier;

  public CompletableDefer(Supplier<? extends CompletableSource> completableSupplier) {
    this.completableSupplier = completableSupplier;
  }

  @Override
  protected void subscribeActual(CompletableObserver observer) {
    CompletableSource c;

    try {
      c =
          Objects.requireNonNull(
              completableSupplier.get(),
              "The completableSupplier returned a null CompletableSource");
    } catch (Throwable e) {
      Exceptions.throwIfFatal(e);
      EmptyDisposable.error(e, observer);
      return;
    }

    c.subscribe(observer);
  }
}
