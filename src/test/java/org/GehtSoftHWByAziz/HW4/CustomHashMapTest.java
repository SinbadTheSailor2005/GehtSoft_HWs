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

  @Test
  void testPutAll() {
    var map = new HashMap<Integer,Integer>();
    map.put(1,1);
    map.put(2,2);
    m.putAll(map);
    assertEquals(1,m.get(1));
    assertEquals(2,m.get(2));
  }

  @Test
  void testContains() {
    m.put(1,1);
    assertTrue(m.containsKey(1));
    assertTrue(m.containsValue(1));
  }

  @Test
  void testGrow() {
    for (int i = 0; i < 100; i++) {
      m.put(i,i);
    }
    assertTrue(true);
    assertEquals(99, m.get(99));
    assertEquals(100, m.size());
  }
}
