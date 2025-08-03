package org.GehtSoftHWByAziz.HW6;

import java.util.*;
import java.util.concurrent.*;

public class CustomExecutorService implements ExecutorService{
  private final int poolSize;
  private final boolean useVirtualThreads;
  private final BlockingDeque<Runnable> tasks;
  private final List<Thread> threads;
  private CustomExecutorService(int poolSize, boolean useVirtualThreads
                               ) {
    this.poolSize = poolSize;
    this.threads  = new ArrayList<>();
    this.useVirtualThreads = useVirtualThreads;
    this.tasks = new LinkedBlockingDeque<>();
    runWorkers(poolSize);
  }

  private void runWorkers(int poolSize) {
    for (int i = 0 ; i < poolSize; i ++) {
      Thread t = new Thread(new Worker ());
      t.start();
      this.threads.add(t);
    }
  }

  public static CustomExecutorService newCustomPlatformThreadPool(int poolSize) {
    return new CustomExecutorService(poolSize, false);
  }
  public static CustomExecutorService newCustomVirtualThreadPool() {
    return new CustomExecutorService(0, true);
  }

  private List<Thread> fillThreadPoolWithVirtualThreads(int poolSize) {
    return null;
  }

  @Override
  public void shutdown() {

  }

  @Override
  public List<Runnable> shutdownNow() {
    return List.of();
  }

  @Override
  public boolean isShutdown() {
    return false;
  }

  @Override
  public boolean isTerminated() {
    return false;
  }

  @Override
  public boolean awaitTermination(
          long l,
          TimeUnit timeUnit) throws InterruptedException {
    return false;
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
  public void execute(Runnable runnable) {

  }
  private class Worker implements Runnable {

    @Override
    public void run() {

      while (!CustomExecutorService.this.isShutdown()) {
        try {
          var task = tasks.poll(1, TimeUnit.SECONDS); // avoid busy waiting
          if (task != null) {
            task.run();
          }
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }


}
