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
package io.reactivex.rxjava3.internal.schedulers;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Implements the Future interface and calls dispose() on cancel() but the other methods are not
 * implemented.
 */
final class DisposeOnCancel implements Future<Object> {

  final Disposable upstream;

  DisposeOnCancel(Disposable d) {
    this.upstream = d;
  }

  @Override
  public boolean cancel(boolean mayInterruptIfRunning) {
    upstream.dispose();
    return false;
  }

  @Override
  public boolean isCancelled() {
    return false;
  }

  @Override
  public boolean isDone() {
    return false;
  }

  @Override
  public Object get() {
    return null;
  }

  @Override
  public Object get(long timeout, @NonNull TimeUnit unit) {
    return null;
  }
}
