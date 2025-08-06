package org.GehtSoftHWByAziz.HW6;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class test {
  public static void main(
          String[] args) throws ExecutionException, InterruptedException {
    // Test 1: Performance comparison
    testPerformanceComparison();

    // Test 2: Concurrent task execution
    testConcurrentExecution();

    // Test 3: Shutdown behavior
    testShutdownBehavior();
  }

  private static void testShutdownBehavior() {
  }

  private static void testConcurrentExecution() {

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
  }

}
