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
package io.reactivex.rxjava3.internal.util;

import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/** Singleton implementing many interfaces as empty. */
public enum EmptyComponent
    implements
        FlowableSubscriber<Object>, Observer<Object>, MaybeObserver<Object>, SingleObserver<Object>,
        CompletableObserver, Subscription, Disposable {
  INSTANCE;

  @SuppressWarnings("unchecked")
  public static <T> Subscriber<T> asSubscriber() {
    return (Subscriber<T>) INSTANCE;
  }

  @SuppressWarnings("unchecked")
  public static <T> Observer<T> asObserver() {
    return (Observer<T>) INSTANCE;
  }

  @Override
  public void dispose() {
    // deliberately no-op
  }

  @Override
  public boolean isDisposed() {
    return true;
  }

  @Override
  public void request(long n) {
    // deliberately no-op
  }

  @Override
  public void cancel() {
    // deliberately no-op
  }

  @Override
  public void onSubscribe(Disposable d) {
    d.dispose();
  }

  @Override
  public void onSubscribe(Subscription s) {
    s.cancel();
  }

  @Override
  public void onNext(Object t) {
    // deliberately no-op
  }

  @Override
  public void onError(Throwable t) {
    RxJavaPlugins.onError(t);
  }

  @Override
  public void onComplete() {
    // deliberately no-op
  }

  @Override
  public void onSuccess(Object value) {
    // deliberately no-op
  }
}
