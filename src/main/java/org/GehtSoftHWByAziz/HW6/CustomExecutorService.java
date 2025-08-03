package org.GehtSoftHWByAziz.HW6;

import java.util.*;
import java.util.concurrent.*;

public class CustomExecutorService implements ExecutorService{
  private final int poolSize;
  private final boolean useVirtualThreads;
  private final Deque<FutureTask<?>> tasks;
  private final List<Thread> pool;
  public CustomExecutorService(int poolSize, boolean useVirtualThreads
                               ) {
    this.poolSize = poolSize;
    this.useVirtualThreads = useVirtualThreads;
    this.tasks = new ArrayDeque<>();
    if (useVirtualThreads)
    this.pool = fillThreadPoolWithVirtualThreads(poolSize);
    else {
      this.pool = fillThreadPoolWithPlatformThreads(poolSize);
    }
  }

  private List<Thread> fillThreadPoolWithPlatformThreads(int poolSize) {
    List<Thread> threads = new ArrayList<>();
    for (int i = 0 ; i < poolSize; i ++) {
      threads.add(new Thread());
    }
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
}
