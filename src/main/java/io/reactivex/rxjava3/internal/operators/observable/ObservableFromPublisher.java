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
package io.reactivex.rxjava3.internal.operators.observable;

import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.internal.subscriptions.SubscriptionHelper;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

public final class ObservableFromPublisher<T> extends Observable<T> {

  final Publisher<? extends T> source;

  public ObservableFromPublisher(Publisher<? extends T> publisher) {
    this.source = publisher;
  }

  @Override
  protected void subscribeActual(final Observer<? super T> o) {
    source.subscribe(new PublisherSubscriber<T>(o));
  }

  static final class PublisherSubscriber<T> implements FlowableSubscriber<T>, Disposable {

    final Observer<? super T> downstream;
    Subscription upstream;

    PublisherSubscriber(Observer<? super T> o) {
      this.downstream = o;
    }

    @Override
    public void onComplete() {
      downstream.onComplete();
    }

    @Override
    public void onError(Throwable t) {
      downstream.onError(t);
    }

    @Override
    public void onNext(T t) {
      downstream.onNext(t);
    }

    @Override
    public void onSubscribe(Subscription s) {
      if (SubscriptionHelper.validate(this.upstream, s)) {
        this.upstream = s;
        downstream.onSubscribe(this);
        s.request(Long.MAX_VALUE);
      }
    }

    @Override
    public void dispose() {
      upstream.cancel();
      upstream = SubscriptionHelper.CANCELLED;
    }

    @Override
    public boolean isDisposed() {
      return upstream == SubscriptionHelper.CANCELLED;
    }
  }
}
