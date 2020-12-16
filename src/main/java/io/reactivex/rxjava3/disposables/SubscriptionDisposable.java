package io.reactivex.rxjava3.disposables;

import io.reactivex.rxjava3.annotations.NonNull;
import org.reactivestreams.Subscription;

/** A 处理{@link Subscription}的一次性容器. */
final class SubscriptionDisposable extends ReferenceDisposable<Subscription> {

  private static final long serialVersionUID = -707001650852963139L;

  SubscriptionDisposable(Subscription value) {
    super(value);
  }

  @Override
  protected void onDisposed(@NonNull Subscription value) {
    value.cancel();
  }
}
