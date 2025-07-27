package org.GehtSoftHWByAziz.HW5.Task1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;


public class Multithreaded_Performance {

  static short[] arr = new short[100_000_000];

  public static long sumWithParallelStream(int threadsCount) {

    try (ForkJoinPool executor = new ForkJoinPool(threadsCount)) {
      return executor.submit(() ->
                      IntStream.range(0, arr.length)
                              .parallel()
                              .mapToLong(i -> (long) arr[i])
                              .sum())
              .get();

    } catch (ExecutionException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public static long sumWithParallelThreads(int threadsCount) {
    List<short[]> subArrays = new ArrayList<>();
    int parts = (arr.length + threadsCount - 1) / threadsCount;
    int from = 0;
    int to = Math.min(arr.length, parts);
    for (int i = 0; i < parts; i++) {
      subArrays.add(Arrays.copyOfRange(arr, from, to));
      from = to;
      to = Math.min(to + parts, arr.length);
    }
    List<FutureTask<Long>> res = new ArrayList<>();

    for (int i = 0; i < threadsCount; i++) {
      SumSubarray task = new SumSubarray(subArrays.get(i));
      FutureTask<Long> adapter = new FutureTask<>(task);
      new Thread(adapter).start();
      res.add(adapter);
    }
    return res.stream()
            .mapToLong(task -> {
              try {
                return task.get(); // WARNING: here we have join() too
              } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
              }
            })
            .sum();

  }

  public static void main(
          String[] args) throws InterruptedException, IOException {
    Arrays.fill(arr, (short) 1);
    var threadCounts = new ArrayList<Integer>(List.of(1, 10, 100, 1000));
    try (FileWriter writer = new FileWriter(
            "src/main/java/org/GehtSoftHWByAziz/HW5/Task1/multithread_perfomance.txt", false)) {
      writer.write("sumWithParallelStream\n");
      for (int c : threadCounts) {

        writer.write("ThreadCounter: " + c + '\n');

        var begin = System.nanoTime();
        sumWithParallelStream(c);
        var end = System.nanoTime() - begin;
        writer.write("Time: " + end / 1_000_000 + "ms\n");

      }
      writer.write("-------------------------------------------\n");
      writer.write("sumWithParallelThreads\n");
      for (int c : threadCounts) {

        writer.write("ThreadCounter: " + c + '\n');

        var begin = System.nanoTime();
        sumWithParallelThreads(c);
        var end = System.nanoTime() - begin;
        writer.write("Time: " + end / 1_000_000 + "ms\n");

      }
    }
  }
}
