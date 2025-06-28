package org.GehtSoftHWByAziz.HW2.Reflection.ReflectionTests;

import org.GehtSoftHWByAziz.HW2.FibonacciAlgorithm.FibonacciAlgorithms;
import org.GehtSoftHWByAziz.HW2.Reflection.AfterEach;
import org.GehtSoftHWByAziz.HW2.Reflection.BeforeEach;
import org.GehtSoftHWByAziz.HW2.Reflection.Test;

import java.util.HashMap;

public class FibonacciAlgorithmTest {
  private HashMap<Integer, Long> cache = new HashMap<>();
  @BeforeEach
   void setup() {
    cache.clear();
    System.out.println("BEFORE EACH was triggered");
  }
  @AfterEach
  void tearDown() {
    System.out.println("AFTER EACH was triggered");
  }

  @Test
  void testFibonacciofrom0To35() {
    for (int i = 0; i <= 35; i++) {
      assert (
              FibonacciAlgorithms.fibonacciMemoized(i, cache) ==
                      FibonacciAlgorithms.fibonacciIterative(i));
      cache.clear();
    }
    System.out.println("TEST was trigerred");
  }
}

