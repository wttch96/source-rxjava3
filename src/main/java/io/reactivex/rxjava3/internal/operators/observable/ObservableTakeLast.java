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

import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.internal.disposables.DisposableHelper;
import java.util.ArrayDeque;

public final class ObservableTakeLast<T> extends AbstractObservableWithUpstream<T, T> {
  final int count;

  public ObservableTakeLast(ObservableSource<T> source, int count) {
    super(source);
    this.count = count;
  }

  @Override
  public void subscribeActual(Observer<? super T> t) {
    source.subscribe(new TakeLastObserver<>(t, count));
  }

  static final class TakeLastObserver<T> extends ArrayDeque<T> implements Observer<T>, Disposable {

    private static final long serialVersionUID = 7240042530241604978L;
    final Observer<? super T> downstream;
    final int count;

    Disposable upstream;

    volatile boolean cancelled;

    TakeLastObserver(Observer<? super T> actual, int count) {
      this.downstream = actual;
      this.count = count;
    }

    @Override
    public void onSubscribe(Disposable d) {
      if (DisposableHelper.validate(this.upstream, d)) {
        this.upstream = d;
        downstream.onSubscribe(this);
      }
    }

    @Override
    public void onNext(T t) {
      if (count == size()) {
        poll();
      }
      offer(t);
    }

    @Override
    public void onError(Throwable t) {
      downstream.onError(t);
    }

    @Override
    public void onComplete() {
      Observer<? super T> a = downstream;
      for (; ; ) {
        if (cancelled) {
          return;
        }
        T v = poll();
        if (v == null) {
          a.onComplete();
          return;
        }
        a.onNext(v);
      }
    }

    @Override
    public void dispose() {
      if (!cancelled) {
        cancelled = true;
        upstream.dispose();
      }
    }

    @Override
    public boolean isDisposed() {
      return cancelled;
    }
  }
}
