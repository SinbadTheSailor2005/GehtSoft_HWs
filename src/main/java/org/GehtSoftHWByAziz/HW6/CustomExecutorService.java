package org.GehtSoftHWByAziz.HW6;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CustomExecutorService implements ExecutorService {
  private final int poolSize;
  private final boolean useVirtualThreads;
  private final BlockingDeque<Runnable> tasks;
  private final List<Thread> threads;
  private volatile boolean isShutdowned = false;
  private volatile boolean isTerminated = false;
  private final ReentrantLock lock = new ReentrantLock();
  private final Condition terminationCondition = lock.newCondition();
  private final AtomicInteger virtualTasks = new AtomicInteger(0);
  private final List<Thread> virtualThreads = new LinkedList<>();

  private CustomExecutorService(int poolSize, boolean useVirtualThreads
  ) {
    this.poolSize = poolSize;
    this.threads = new ArrayList<>();
    this.useVirtualThreads = useVirtualThreads;
    this.tasks = new LinkedBlockingDeque<>();
    if (!useVirtualThreads)
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
  public boolean awaitTermination(
          long timeout,
          TimeUnit timeUnit) throws InterruptedException {
    long millis = timeUnit.toMillis(timeout);
    long nanos = timeUnit.toNanos(timeout);
    // used lock to make condition check and
    if (useVirtualThreads) {
      Thread.sleep(millis);
      if (checkTermination()) {
        return true;
      } else {
        return false;
      }
    }
    lock.lock();
    try {
      while (!isTerminated) { // защита от ложного пробуждения
//        A thread can also wake up without being notified, interrupted, or "
//                timing out, a so-called spurious wakeup.
        if (nanos <= 0) {
          return false;
        }
        // if time is out
        // returns value <=0
        // if condition is met then isTerminated is set to true
        nanos = terminationCondition.awaitNanos(nanos);
      }
      return true;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public <T> Future<T> submit(Callable<T> callable) {
    // RunnableFuture<T> implements RUNNABLE and FUTURE
    RunnableFuture<T> ftask = new FutureTask<>(callable);

    execute(ftask);
    return ftask;
  }

  @Override
  public <T> Future<T> submit(Runnable runnable, T t) {
    RunnableFuture<T> ftask = new FutureTask<>(runnable, t);
    execute(ftask);
    return ftask;
  }

  @Override
  public Future<?> submit(Runnable runnable) {
    RunnableFuture<Void> ftask = new FutureTask<>(runnable, null);
    execute(ftask);
    return ftask;
  }

  @Override
  public <T> List<Future<T>> invokeAll(
          Collection<? extends Callable<T>> collection) throws InterruptedException {
    List<Future<T>> tasks = new ArrayList<>();
    for (var task : collection) {
      var future = submit(task);
      tasks.add(future);
    }
    for (var future : tasks) {
      try {
        future.get();
      } catch (ExecutionException e) {

      }
    }
    return tasks;
  }

  @Override
  public <T> List<Future<T>> invokeAll(
          Collection<? extends Callable<T>> collection, long l,
          TimeUnit timeUnit) throws InterruptedException {
    long nanos = timeUnit.toNanos(l);
    long end = System.nanoTime() + nanos;
    List<Future<T>> tasks = new ArrayList<>();
    for (var task : collection) {
      var future = submit(task);
      tasks.add(future);
    }
    for (var future : tasks) {
      try {
        future.get(end - System.nanoTime(), TimeUnit.NANOSECONDS);
      } catch (ExecutionException e) {
      } catch (TimeoutException e) {
        return tasks;
      }
      return tasks;
    }
    return tasks;
  }

  @Override
  public <T> T invokeAny(
          Collection<? extends Callable<T>> collection) throws InterruptedException, ExecutionException {
    List<Future<T>> tasks = new ArrayList<>();
    for (var task : collection) {
      var future = submit(task);
      tasks.add(future);
    }
    while (true) {
      for (var future : tasks) {
        if (future.isDone()) {
          return future.get();
        }
      }
    }
  }

  @Override
  public <T> T invokeAny(
          Collection<? extends Callable<T>> collection, long timeOut,
          TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
    long nanos = timeUnit.toNanos(timeOut);
    long end = System.nanoTime() + nanos;
    List<Future<T>> tasks = new ArrayList<>();
    for (var task : collection) {
      var future = submit(task);
      tasks.add(future);
    }
    while (true) {
      for (var future : tasks) {
        if (future.isDone()) {
          return future.get();
        }
        if (end - System.nanoTime() <= 0) {
          throw new TimeoutException("Time is out");
        }
      }
    }
  }

  @Override
  public void execute(Runnable task) {
    if (isShutdown()) {
      throw new RejectedExecutionException("shutdown was called, no more new " +
              "tasks");
    }
    if (this.useVirtualThreads) {
      Runnable taskWrapper = () -> {
        try {
          task.run();

        } finally {

          virtualTasks.decrementAndGet();
        }
      };
      Thread t = Thread.ofVirtual()
              .unstarted(taskWrapper);
      virtualTasks.incrementAndGet();
      t.start();
    } else {
      try {
        this.tasks.offer(task, 1, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private boolean checkTermination() {
    if (virtualTasks.get() == 0 && isShutdowned) {
      lock.lock();
      try {
        isTerminated = true;
        return true;
      } finally {
        lock.unlock();
      }
    }
    return false;
  }

  private class Worker implements Runnable {

    @Override
    public void run() {
      try {
        // first condition for shutdownNow,
        // second for shutdown, since after all tasks are done
        // the thread should be terminated
        while (!Thread.currentThread()
                .isInterrupted() && !(isShutdown() && tasks.isEmpty())) {
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
