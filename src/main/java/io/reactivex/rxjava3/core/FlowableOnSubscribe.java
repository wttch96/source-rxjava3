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
package io.reactivex.rxjava3.core;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * A functional interface that has a {@code subscribe()} method that receives an instance of a
 * {@link FlowableEmitter} instance that allows pushing events in a backpressure-safe and
 * cancellation-safe manner.
 *
 * @param <T> the value type pushed
 */
@FunctionalInterface
public interface FlowableOnSubscribe<@NonNull T> {

  /**
   * Called for each {@link org.reactivestreams.Subscriber Subscriber} that subscribes.
   *
   * @param emitter the safe emitter instance, never {@code null}
   * @throws Throwable on error
   */
  void subscribe(@NonNull FlowableEmitter<T> emitter) throws Throwable;
}
