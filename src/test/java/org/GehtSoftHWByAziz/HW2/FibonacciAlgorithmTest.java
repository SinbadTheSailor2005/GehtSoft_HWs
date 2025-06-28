package org.GehtSoftHWByAziz.HW2;


import org.GehtSoftHWByAziz.HW2.FibonacciAlgorithm.FibonacciAlgorithms;
import org.junit.jupiter.api.*;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FibonacciAlgorithmTest {
  private HashMap<Integer, Long> cache = new HashMap<>();
  @BeforeEach
  void setup() {
    cache.clear();
  }
  @Test
  void  testFibonacciofrom0To35() {
    for (int i = 0 ; i <= 35; i ++) {
      assertEquals(FibonacciAlgorithms.fibonacciMemoized(i,cache),
              FibonacciAlgorithms.fibonacciIterative(i) );
      cache.clear();
    }
  }
}
