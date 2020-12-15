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
package io.reactivex.rxjava3.internal.operators.flowable;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.MaybeSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.internal.operators.flowable.FlowableFlatMapMaybe.FlatMapMaybeSubscriber;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

/**
 * Maps upstream values into MaybeSources and merges their signals into one sequence.
 *
 * @param <T> the source value type
 * @param <R> the result value type
 */
public final class FlowableFlatMapMaybePublisher<T, R> extends Flowable<R> {

  final Publisher<T> source;

  final Function<? super T, ? extends MaybeSource<? extends R>> mapper;

  final boolean delayErrors;

  final int maxConcurrency;

  public FlowableFlatMapMaybePublisher(
      Publisher<T> source,
      Function<? super T, ? extends MaybeSource<? extends R>> mapper,
      boolean delayError,
      int maxConcurrency) {
    this.source = source;
    this.mapper = mapper;
    this.delayErrors = delayError;
    this.maxConcurrency = maxConcurrency;
  }

  @Override
  protected void subscribeActual(Subscriber<? super R> s) {
    source.subscribe(new FlatMapMaybeSubscriber<>(s, mapper, delayErrors, maxConcurrency));
  }
}
