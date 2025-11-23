package org.GehtSoftHWByAziz.HW2.FibonacciAlgorithm;

import java.lang.reflect.GenericDeclaration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Benchmark {


  private static void testRec(int n ) {
    Runtime runtime = Runtime.getRuntime();
    HashMap<Integer, Long> map = new HashMap<>();
    System.gc();
    long beforeMem = runtime.totalMemory() - runtime.freeMemory();
    long start = System.nanoTime();

    FibonacciAlgorithms.fibonacciMemoized(n, map);

    long stop = System.nanoTime();
    System.gc();
    long afterMem = runtime.totalMemory() - runtime.freeMemory();

    double duration = (stop - start) /1_000_000.0;
    double memUsage = (afterMem - beforeMem);

    System.out.printf("""
                      N = %s
                      Total execution time = %s ms
                      Total memory Usage = %s b
                      """, n,duration, memUsage);
  }
  private static void testIterative(int n ) {
    Runtime runtime = Runtime.getRuntime();
    System.gc();

    long beforeMem = runtime.totalMemory() - runtime.freeMemory();
    long start = System.nanoTime();

    FibonacciAlgorithms.fibonacciIterative(n);

    long stop = System.nanoTime();
    System.gc();

    long afterMem = runtime.totalMemory() - runtime.freeMemory();

    double duration = (stop - start) /1_000_000.0;
    double memUsage = (afterMem - beforeMem) / (1024.0 * 1024.0);

    System.out.printf("""
                      N = %s
                      Total execution time = %s ms
                      Total memory Usage = %s mb
                      """, n,duration, memUsage);
  }


  public static void main (String [] args) {
    ArrayList<Integer> inputs = new ArrayList<>(List.of(10,20,30,35));
    System.out.println("Recursive Fibonacci testing");
    for (Integer i : inputs) {
      testRec(i);
    }

    System.out.println("\n\nIterative Fibonacci testing");
    for (Integer i : inputs) {
      testIterative(i);
    }
  }
}
