package org.GehtSoftHWByAziz.HW5.Task2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Task2 {


  private static void testVirtualThreads() {
    try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
      long start = System.nanoTime();
      Runtime runtime = Runtime.getRuntime();
      long beforeMem = runtime.totalMemory() - runtime.freeMemory();
      List<Future<?>> res = new ArrayList<>();
      for (int i = 0; i < 8_000; i++) {
        Runnable task = new SleepTask();
        res.add(executor.submit(task));
      }
      for (var r : res) {
        r.get();
      }
      long afterMem = runtime.totalMemory() - runtime.freeMemory();
      long memTaken = afterMem - beforeMem;
      long end = System.nanoTime();
      long duration = end - start;
      System.out.println("Virtual Threads Result");
      String result = String.format("""
                                    Add Time: %s ms
                                    Memory taken: %s mb
                                    """, duration / 1_000_000.0,
              memTaken / (1024.0 * 1024.0));
      System.out.println(result);
    } catch (ExecutionException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private static void testPlatformThread() throws InterruptedException {

    long start = System.nanoTime();
    Runtime runtime = Runtime.getRuntime();
    long beforeMem = runtime.totalMemory() - runtime.freeMemory();
    List<Thread> threads = new ArrayList<>();
    for (int i = 0; i < 8_000; i++) {
      Runnable task = new SleepTask();
      Thread t = new Thread(task);
      threads.add(t);
      t.start();
    }
    for (var t : threads) {
      t.join();
    }
    long afterMem = runtime.totalMemory() - runtime.freeMemory();
    long memTaken = afterMem - beforeMem;
    long end = System.nanoTime();
    long duration = end - start;
    System.out.println("Platform Threads Results");
    String result = String.format("""
                                  Add Time: %s ms
                                  Memory taken: %s mb
                                  """, duration / 1_000_000.0,
            memTaken / (1024.0 * 1024.0));
    System.out.println(result);

  }


  public static void main(String[] args) throws InterruptedException {
    testPlatformThread();
    System.out.println("-".repeat(100));
    testVirtualThreads();
  }
}
