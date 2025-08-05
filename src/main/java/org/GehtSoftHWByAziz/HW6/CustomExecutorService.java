package org.GehtSoftHWByAziz.HW6;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CustomExecutorService implements ExecutorService {
  private final int poolSize;
  private final boolean useVirtualThreads;
  private final BlockingDeque<Runnable> tasks;
  private final List<Thread> threads;
  private boolean isShutdowned = false;
  private boolean isTerminated = false;
  private final ReentrantLock lock = new ReentrantLock();
  private final Condition terminationCondition = lock.newCondition();

  private CustomExecutorService(int poolSize, boolean useVirtualThreads
  ) {
    this.poolSize = poolSize;
    this.threads = new ArrayList<>();
    this.useVirtualThreads = useVirtualThreads;
    this.tasks = new LinkedBlockingDeque<>();
    runWorkers(poolSize);
  }

  private void runWorkers(int poolSize) {
    for (int i = 0; i < poolSize; i++) {
      Thread t = new Thread(new Worker());
      t.start();
      this.threads.add(t);
    }
  }

  public static CustomExecutorService newCustomPlatformThreadPool(
          int poolSize) {
    return new CustomExecutorService(poolSize, false);
  }

  public static CustomExecutorService newCustomVirtualThreadPool() {
    return new CustomExecutorService(0, true);
  }

  @Override
  public void shutdown() {
    this.isShutdowned = true;
  }

  @Override
  public List<Runnable> shutdownNow() {
    shutdown();
    for (var thread : threads) {
      thread.interrupt();
    }
    List<Runnable> t = new ArrayList<>(this.tasks.stream()
            .toList());
    tasks.clear();
    return t;
  }

  @Override
  public boolean isShutdown() {
    return this.isShutdowned;
  }

  @Override
  public boolean isTerminated() {
    return this.isTerminated;
  }

  @Override
  /*
  * need rework Worker to somehow
  * terminate ic case there are no more tasks
  * so, it the method will busy waiting for
  * specific state
  * use
  * private final ReentrantLock lock = new ReentrantLock();
    private final Condition terminationCondition = lock.newCondition();
    * Condition for stopping awaitTermination
  * */
  public boolean awaitTermination(
          long timeout,
          TimeUnit timeUnit) throws InterruptedException {
    long nanos = timeUnit.toNanos(timeout);
    lock.lock();
    try {
      while (!isTerminated) {
        if (nanos <= 0) {
          return false;
        }
        // if time is out
        // returns value <=0
        nanos = terminationCondition.awaitNanos(nanos);
      }
      return true;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public <T> Future<T> submit(Callable<T> callable) {
    return null;
  }

  @Override
  public <T> Future<T> submit(Runnable runnable, T t) {
    return null;
  }

  @Override
  public Future<?> submit(Runnable runnable) {
    return null;
  }

  @Override
  public <T> List<Future<T>> invokeAll(
          Collection<? extends Callable<T>> collection) throws InterruptedException {
    return List.of();
  }

  @Override
  public <T> List<Future<T>> invokeAll(
          Collection<? extends Callable<T>> collection, long l,
          TimeUnit timeUnit) throws InterruptedException {
    return List.of();
  }

  @Override
  public <T> T invokeAny(
          Collection<? extends Callable<T>> collection) throws InterruptedException, ExecutionException {
    return null;
  }

  @Override
  public <T> T invokeAny(
          Collection<? extends Callable<T>> collection, long l,
          TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
    return null;
  }

  @Override
  public void execute(Runnable task) {
    if (isShutdown()) {
      throw new RejectedExecutionException("shutdown was called, no more new " +
              "tasks");
    }
    if (this.useVirtualThreads) {
      Thread.ofVirtual()
              .start(task);
    } else {
      try {
        this.tasks.offer(task, 1, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private class Worker implements Runnable {

    @Override
    public void run() {
      try {
        while (!Thread.currentThread()
                .isInterrupted() || !(isShutdown() && tasks.isEmpty())) {
          try {
            var task = tasks.poll(1, TimeUnit.SECONDS); // avoid busy waiting
            if (task != null) {
              task.run();
            }
          } catch (InterruptedException e) {
            Thread.currentThread()
                    .interrupt(); // the flag "interrupted" was
            // reset during exception
          }
        }

      } finally {
        lock.lock();
        try {
          threads.remove(Thread.currentThread());
          if (threads.isEmpty() && isShutdowned) {
            isTerminated = true;
            terminationCondition.signalAll();
          }
        } finally {
          lock.unlock();
        }
      }
    }
  }


}
