package org.GehtSoftHWByAziz;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PerfomanceTest {

  private static void benchmarkTestAdd(List<Integer> list) {
    long start = System.nanoTime();
    Runtime runtime = Runtime.getRuntime();
    long beforeMem = runtime.totalMemory() - runtime.freeMemory();
    for (int i = 0; i < 1000000; i++) {
      list.add(i);
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
            """, list.getClass(), duration/1_000_000.0, memTaken  / (1024.0 * 1024.0)
    );
    System.out.println(result);

  }
  private static void benchmarkTestRemove(List<Integer> list){
    long start = System.nanoTime();
    Runtime runtime =Runtime.getRuntime();

    long beforeMem = runtime.totalMemory() - runtime.freeMemory();

    for (int i = 0 ; i < 10000; i ++) {
      list.add(i);
    }
    for (int i = 0; i < 10000; i ++) {
      list.remove(0);
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
            """, list.getClass(), duration/1_000_000.0, memTaken  / (1024.0 * 1024.0)
    );
    System.out.println(result);


  }

  public static void main(String[] args) {
    var myList = new CustomList<Integer>();
    var linkedList = new LinkedList<Integer>();
    var arrayList = new ArrayList<Integer>();
    benchmarkTestAdd(myList);
    benchmarkTestAdd(linkedList);
    benchmarkTestAdd(arrayList);

    System.out.println("-----------------------------------------");

    benchmarkTestRemove(linkedList);
    benchmarkTestRemove(arrayList);
    benchmarkTestRemove(myList);
  }
}
