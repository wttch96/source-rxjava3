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
import io.reactivex.rxjava3.exceptions.CompositeException;
import io.reactivex.rxjava3.exceptions.Exceptions;
import io.reactivex.rxjava3.functions.BiPredicate;
import io.reactivex.rxjava3.internal.subscriptions.SubscriptionArbiter;
import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableRetryBiPredicate<T> extends AbstractFlowableWithUpstream<T, T> {
  final BiPredicate<? super Integer, ? super Throwable> predicate;

  public FlowableRetryBiPredicate(
      Flowable<T> source, BiPredicate<? super Integer, ? super Throwable> predicate) {
    super(source);
    this.predicate = predicate;
  }

  @Override
  public void subscribeActual(Subscriber<? super T> s) {
    SubscriptionArbiter sa = new SubscriptionArbiter(false);
    s.onSubscribe(sa);

    RetryBiSubscriber<T> rs = new RetryBiSubscriber<>(s, predicate, sa, source);
    rs.subscribeNext();
  }

  static final class RetryBiSubscriber<T> extends AtomicInteger implements FlowableSubscriber<T> {

    private static final long serialVersionUID = -7098360935104053232L;

    final Subscriber<? super T> downstream;
    final SubscriptionArbiter sa;
    final Publisher<? extends T> source;
    final BiPredicate<? super Integer, ? super Throwable> predicate;
    int retries;

    long produced;

    RetryBiSubscriber(
        Subscriber<? super T> actual,
        BiPredicate<? super Integer, ? super Throwable> predicate,
        SubscriptionArbiter sa,
        Publisher<? extends T> source) {
      this.downstream = actual;
      this.sa = sa;
      this.source = source;
      this.predicate = predicate;
    }

    @Override
    public void onSubscribe(Subscription s) {
      sa.setSubscription(s);
    }

    @Override
    public void onNext(T t) {
      produced++;
      downstream.onNext(t);
    }

    @Override
    public void onError(Throwable t) {
      boolean b;
      try {
        b = predicate.test(++retries, t);
      } catch (Throwable e) {
        Exceptions.throwIfFatal(e);
        downstream.onError(new CompositeException(t, e));
        return;
      }
      if (!b) {
        downstream.onError(t);
        return;
      }
      subscribeNext();
    }

    @Override
    public void onComplete() {
      downstream.onComplete();
    }

    /** Subscribes to the source again via trampolining. */
    void subscribeNext() {
      if (getAndIncrement() == 0) {
        int missed = 1;
        for (; ; ) {
          if (sa.isCancelled()) {
            return;
          }

          long p = produced;
          if (p != 0L) {
            produced = 0L;
            sa.produced(p);
          }

          source.subscribe(this);

          missed = addAndGet(-missed);
          if (missed == 0) {
            break;
          }
        }
      }
    }
  }
}
