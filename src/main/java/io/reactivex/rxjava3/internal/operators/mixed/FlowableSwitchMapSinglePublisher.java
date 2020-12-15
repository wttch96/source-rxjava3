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
package io.reactivex.rxjava3.internal.operators.mixed;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.rxjava3.functions.Function;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

/**
 * Switch between subsequent {@link SingleSource}s emitted by a {@link Publisher}. Reuses {@link
 * FlowableSwitchMapSingle} internals.
 *
 * @param <T> the upstream value type
 * @param <R> the downstream value type
 * @since 3.0.0
 */
public final class FlowableSwitchMapSinglePublisher<T, R> extends Flowable<R> {

  final Publisher<T> source;

  final Function<? super T, ? extends SingleSource<? extends R>> mapper;

  final boolean delayErrors;

  public FlowableSwitchMapSinglePublisher(
      Publisher<T> source,
      Function<? super T, ? extends SingleSource<? extends R>> mapper,
      boolean delayErrors) {
    this.source = source;
    this.mapper = mapper;
    this.delayErrors = delayErrors;
  }

  @Override
  protected void subscribeActual(Subscriber<? super R> s) {
    source.subscribe(
        new FlowableSwitchMapSingle.SwitchMapSingleSubscriber<>(s, mapper, delayErrors));
  }
}
