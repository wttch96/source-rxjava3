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
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.internal.subscriptions.EmptySubscription;
import io.reactivex.rxjava3.internal.subscriptions.SubscriptionHelper;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableTake<T> extends AbstractFlowableWithUpstream<T, T> {

  final long n;

  public FlowableTake(Flowable<T> source, long n) {
    super(source);
    this.n = n;
  }

  @Override
  protected void subscribeActual(Subscriber<? super T> s) {
    source.subscribe(new TakeSubscriber<>(s, n));
  }

  static final class TakeSubscriber<T> extends AtomicLong
      implements FlowableSubscriber<T>, Subscription {

    private static final long serialVersionUID = 2288246011222124525L;

    final Subscriber<? super T> downstream;

    long remaining;

    Subscription upstream;

    TakeSubscriber(Subscriber<? super T> actual, long remaining) {
      this.downstream = actual;
      this.remaining = remaining;
      lazySet(remaining);
    }

    @Override
    public void onSubscribe(Subscription s) {
      if (SubscriptionHelper.validate(this.upstream, s)) {
        if (remaining == 0L) {
          s.cancel();
          EmptySubscription.complete(downstream);
        } else {
          this.upstream = s;
          downstream.onSubscribe(this);
        }
      }
    }

    @Override
    public void onNext(T t) {
      long r = remaining;
      if (r > 0L) {
        remaining = --r;
        downstream.onNext(t);
        if (r == 0L) {
          upstream.cancel();
          downstream.onComplete();
        }
      }
    }

    @Override
    public void onError(Throwable t) {
      if (remaining > 0L) {
        remaining = 0L;
        downstream.onError(t);
      } else {
        RxJavaPlugins.onError(t);
      }
    }

    @Override
    public void onComplete() {
      if (remaining > 0L) {
        remaining = 0L;
        downstream.onComplete();
      }
    }

    @Override
    public void request(long n) {
      if (SubscriptionHelper.validate(n)) {
        for (; ; ) {
          long r = get();
          if (r == 0L) {
            break;
          }
          long toRequest = Math.min(r, n);
          long u = r - toRequest;
          if (compareAndSet(r, u)) {
            upstream.request(toRequest);
            break;
          }
        }
      }
    }

    @Override
    public void cancel() {
      upstream.cancel();
    }
  }
}
