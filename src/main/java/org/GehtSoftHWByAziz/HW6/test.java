package org.GehtSoftHWByAziz.HW6;

import javax.swing.plaf.IconUIResource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

public class test {
  public static void main(
          String[] args) throws ExecutionException, InterruptedException {
    // Test 1: Performance comparison
    testPerformanceComparison();
    System.out.println("\n" + "-".repeat(20) + "\n");
    // Test 2: Concurrent task execution
    testConcurrentExecution();
    System.out.println("\n" + "-".repeat(20) + "\n");
    // Test 3: Shutdown behavior
    testShutdownBehavior();
  }

  private static void testShutdownBehavior() {
    ExecutorService pService =
            CustomExecutorService.newCustomPlatformThreadPool(10);
    ExecutorService vService =
            CustomExecutorService.newCustomVirtualThreadPool();
    Runnable task = () -> {
      try {
        Thread.sleep(10000);
      } catch (InterruptedException e) {
        System.out.println("Task interrupted");
      }
    };
    Future<?> pFuture = pService.submit(task);
    Future<?> vFuture = vService.submit(task);
    pService.shutdown();
    vService.shutdown();
    try {
      pService.submit(task);
    } catch (RejectedExecutionException e) {
      System.out.println("Platform Thread Pool: Task rejected after shutdown");
    }
    try {

      vService.submit(task);
    } catch (RejectedExecutionException e) {
      System.out.println("Virtual Thread Pool: Task rejected after shutdown");
    }
    pService.shutdownNow();
    vService.shutdownNow();
  }
  static AtomicInteger counter = new AtomicInteger(0);
  private static void testConcurrentExecution() throws ExecutionException, InterruptedException {
    ExecutorService pService =
            CustomExecutorService.newCustomPlatformThreadPool(10);
    Runnable task = () -> {
      counter.incrementAndGet();
    };
    List<Future<?>> futures = new ArrayList<>();
    for (int i = 0; i < 1000; i++) {
      futures.add(pService.submit(task));
    }
    for (var f : futures) {
        f.get();
    }
    System.out.println("Platform Thread Counter: ");
    System.out.println("1000 == " + counter.get());
    pService.shutdown();
    counter.set(0);
    ExecutorService vservice =
            CustomExecutorService.newCustomVirtualThreadPool();
    futures.clear();
    for (int i = 0; i < 1000; i++) {
      futures.add(vservice.submit(task));
    }
    for (var f : futures) {
      f.get();
    }
    System.out.println("Virtual Thread Counter: ");
    System.out.println("1000 == " + counter.get());
  }

  private static void testPerformanceComparison() throws ExecutionException, InterruptedException {
    List<Integer> poolSize = List.of(10, 50, 100, 500);
    for (var pool : poolSize) {
      ExecutorService pService =
              CustomExecutorService.newCustomPlatformThreadPool(pool);

      List<Future> pTasks = new ArrayList<>();
      long start = System.nanoTime();
      long memBefore = Runtime.getRuntime()
              .totalMemory() - Runtime.getRuntime()
              .freeMemory();
      for (int i = 0; i < 1000; i++) {
        pTasks.add(pService.submit(new TestTask()));
      }
      for (var t : pTasks) {
        t.get();
      }
      long finish = System.nanoTime();
      long memAfter = Runtime.getRuntime()
              .totalMemory() - Runtime.getRuntime()
              .freeMemory();
      long memTaken = memAfter - memBefore;
      long duration = finish - start;
      System.out.printf(
              """
              Platform Thread Pool Size: %d
              Execution Time: %.2f ms
              Memory Used: %.2f mb
              """, pool, duration / 1_000_000.0, memTaken / (1024.0 * 1024.0)
      );
      pService.shutdown();
    }
    System.out.println("\n" + "-".repeat(20 )+ "\n");
    ExecutorService vService =
            CustomExecutorService.newCustomVirtualThreadPool();
    List<Future> vTasks = new ArrayList<>();
    long start = System.nanoTime();
    long memBefore = Runtime.getRuntime()
            .totalMemory() - Runtime.getRuntime()
            .freeMemory
                    ();
    for (int i = 0; i < 1000; i++) {
        vTasks.add(vService.submit(new TestTask()));
    }
    for (var t : vTasks) {
      t.get();
    }
    long end = System.nanoTime();
    long memAfter = Runtime.getRuntime()
            .totalMemory() - Runtime.getRuntime()
            .freeMemory();
    long memTaken = memAfter - memBefore;
    long duration = end - start;
    System.out.printf("""
                      Virtual Thread
                      Execution Time: %.2f ms
                      Memory Used: %.2f mb
                      """, duration / 1_000_000.0, memTaken / (1024.0 * 1024.0));
    vService.shutdown();
  }

}
