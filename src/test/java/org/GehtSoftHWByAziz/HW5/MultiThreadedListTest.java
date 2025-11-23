package org.GehtSoftHWByAziz.HW5;

import org.GehtSoftHWByAziz.HW1.CustomList;
import org.GehtSoftHWByAziz.HW5.Task4.CustomListRWLock;
import org.GehtSoftHWByAziz.HW5.Task4.CustomListSynchronized;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadedListTest {


  static List<List<Integer>> allLists() {
    return List.of(
            new CustomList<>(), new CustomListRWLock<>(),
            new CustomListSynchronized<>()
    );
  }

  static List<List<Integer>> threadSafeLists() {
    return List.of(new CustomListRWLock<>(), new CustomListSynchronized<>()
    );
  }

  @ParameterizedTest
  @MethodSource("allLists")
  void correctnessTest(List<Integer> list) throws InterruptedException {


    for (int i = 0; i < 100; i++) {
      Thread t1 = new Thread(() -> {
        for (int j = 0; j < 1_000_000; j++) {
          list.add(1);
        }
      }
      );

      Thread t2 = new Thread(() -> {
        for (int j = 0; j < 1_000_000; j++) {
          list.add(1);
        }
      }
      );

      t1.start();
      t2.start();
      t1.join();
      t2.join();
    }
    int expectedSize = 2_000_000 * 100;
    if (!(list instanceof CustomList<Integer>)) {
      assertEquals(expectedSize, list.size());
    } else {
      assertNotEquals(expectedSize, list.size());
    }
  }

  @ParameterizedTest
  @MethodSource("allLists")
  void perfomanceTest(List<Integer> list) throws InterruptedException {
    Thread t1 = new Thread(() -> {
      for (int j = 0; j < 1_000_000; j++) {
        list.add(1);
      }
    }
    );

    Thread t2 = new Thread(() -> {
      for (int j = 0; j < 1_000_000; j++) {
        list.add(1);
      }
    }
    );
    long start = System.nanoTime();
    Runtime runtime = Runtime.getRuntime();
    long beforeMem = runtime.totalMemory() - runtime.freeMemory();
    t1.start();
    t2.start();
    t1.join();
    t2.join();
    long afterMem = runtime.totalMemory() - runtime.freeMemory();
    long memTaken = afterMem - beforeMem;
    long end = System.nanoTime();
    long duration = end - start;
    String result = String.format(
            """
            Add Time: %s ms
            Memory taken: %s mb
            """, duration / 1_000_000.0,
            memTaken / (1024.0 * 1024.0)
    );
    System.out.println(result);
    if (!(list instanceof CustomList<Integer>)) {
      assertEquals(2_000_000, list.size());
    } else {
      assertNotEquals(2_000_000, list.size());
    }

  }
}
