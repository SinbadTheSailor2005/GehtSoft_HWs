package org.GehtSoftHWByAziz.HW4;
import org.GehtSoftHWByAziz.HW2.FibonacciAlgorithm.FibonacciAlgorithms;
import org.junit.jupiter.api.*;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomHashMapTest {
  CustomHashMap<Integer, Integer> m;
  @BeforeEach
  void setUp() {
    m = new CustomHashMap<>();
  }
  @AfterEach
  void tearDown() {
    m.clear();
  }
  @Test
  void testAdd() {
    m.put(1, 2);
    m.put (2,3);
    assertEquals(2, m.get(1));
    assertEquals(3,m.get(2));
  }

  @Test
  void testRemove() {
    m.put(1,1);
    m.put(2,2);
    m.remove(1);
    m.remove(2);
    assertTrue(m.isEmpty());
    assertEquals(0, m.size());
  }
}
