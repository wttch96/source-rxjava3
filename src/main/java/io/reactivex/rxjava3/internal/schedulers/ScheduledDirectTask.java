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

import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;

/**
 * A Callable to be submitted to an ExecutorService that runs a Runnable action and manages
 * completion/cancellation.
 *
 * @since 2.0.8
 */
public final class ScheduledDirectTask extends AbstractDirectTask implements Callable<Void> {

  private static final long serialVersionUID = 1811839108042568751L;

  public ScheduledDirectTask(Runnable runnable) {
    super(runnable);
  }

  @Override
  public Void call() {
    runner = Thread.currentThread();
    try {
      try {
        runnable.run();
      } finally {
        lazySet(FINISHED);
        runner = null;
      }
    } catch (Throwable ex) {
      // Exceptions.throwIfFatal(e); nowhere to go
      RxJavaPlugins.onError(ex);
      throw ex;
    }
    return null;
  }
}
