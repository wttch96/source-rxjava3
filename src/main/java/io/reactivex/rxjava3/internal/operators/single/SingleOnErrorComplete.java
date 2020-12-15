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

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.internal.operators.maybe.MaybeOnErrorComplete;

/**
 * Emits an onComplete if the source emits an onError and the predicate returns true for that
 * Throwable.
 *
 * @param <T> the value type
 * @since 3.0.0
 */
public final class SingleOnErrorComplete<T> extends Maybe<T> {

  final Single<T> source;

  final Predicate<? super Throwable> predicate;

  public SingleOnErrorComplete(Single<T> source, Predicate<? super Throwable> predicate) {
    this.source = source;
    this.predicate = predicate;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
    source.subscribe(new MaybeOnErrorComplete.OnErrorCompleteMultiObserver<T>(observer, predicate));
  }
}
