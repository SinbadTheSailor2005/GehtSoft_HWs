package org.GehtSoftHWByAziz.HW4;

import org.GehtSoftHWByAziz.HW1.CustomList;

import java.util.*;

public class PerfomanceTest {

  private static void benchmarkTestAdd(Map<Integer, Integer> m) {
    long start = System.nanoTime();
    Runtime runtime = Runtime.getRuntime();
    long beforeMem = runtime.totalMemory() - runtime.freeMemory();
    for (int i = 0; i < 1000000; i++) {
      m.put(i, i);
    }
    long afterMem = runtime.totalMemory() - runtime.freeMemory();
    long memTaken = afterMem - beforeMem;
    long end = System.nanoTime();
    long duration = end - start; // Время в наносекундах

    String result = String.format(
            """
            %s
            Add Time: %s ms
            Memory taken: %s mb
            """, m.getClass(), duration/1_000_000.0,
            memTaken  / (1024.0 * 1024.0)
    );
    System.out.println(result);

  }
  private static void benchmarkTestRemove(Map<Integer, Integer> map){
    long start = System.nanoTime();
    Runtime runtime =Runtime.getRuntime();

    long beforeMem = runtime.totalMemory() - runtime.freeMemory();

    for (int i = 0 ; i < 10000; i ++) {
      map.put(i, i);
    }
    for (int i = 0; i < 10000; i ++) {
      map.remove(0);
    }
    long end = System.nanoTime();
    long duration = end - start;

    long afterMem = runtime.totalMemory() - runtime.freeMemory();
    long memTaken = afterMem - beforeMem;


    String result = String.format(
            """
            %s
            Remove Time: %s ms
            Memory taken: %s mb
            """, map.getClass(), duration/1_000_000.0,
            memTaken  / (1024.0 * 1024.0)
    );
    System.out.println(result);


  }

  public static void main(String[] args) {
    var myMap = new CustomHashMap<Integer, Integer>();
    var map = new HashMap<Integer, Integer>();
    benchmarkTestAdd(myMap);
    benchmarkTestAdd(map);


    System.out.println("-----------------------------------------");

    benchmarkTestRemove(myMap);
    benchmarkTestRemove(map);
  }
}
