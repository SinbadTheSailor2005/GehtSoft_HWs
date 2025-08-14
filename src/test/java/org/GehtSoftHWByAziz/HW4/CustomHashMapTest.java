package org.GehtSoftHWByAziz.HW4;
import org.GehtSoftHWByAziz.HW2.FibonacciAlgorithm.FibonacciAlgorithms;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.params.ParameterizedTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomHashMapTest {
 static List<Map<Integer, Integer>> mapImplementations() {
   return List.of(new CustomHashMap<Integer,Integer>(),
           new HashMap<Integer,Integer>() );
 }
 @ParameterizedTest
@MethodSource("mapImplementations")
  void testAdd (Map<Integer,Integer>m ) {
    m.put(1, 2);
    m.put (2,3);
    assertEquals(2, m.get(1));
    assertEquals(3,m.get(2));
  }

  @ParameterizedTest
@MethodSource ("mapImplementations")
  void testRemove (Map<Integer,Integer>m ) {
    m.put(1,1);
    m.put(2,2);
    m.remove(1);
    m.remove(2);
    assertTrue(m.isEmpty());
    assertEquals(0, m.size());
  }

  @ParameterizedTest
@MethodSource ("mapImplementations")
  void testPutAll (Map<Integer,Integer>m ) {
    var map = new HashMap<Integer,Integer>();
    map.put(1,1);
    map.put(2,2);
    m.putAll(map);
    assertEquals(1,m.get(1));
    assertEquals(2,m.get(2));
  }

  @ParameterizedTest
@MethodSource ("mapImplementations")
  void testContains (Map<Integer,Integer>m ) {
    m.put(1,1);
    assertTrue(m.containsKey(1));
    assertTrue(m.containsValue(1));
  }

  @ParameterizedTest
@MethodSource ("mapImplementations")
  void testGrow (Map<Integer,Integer>m ) {
    for (int i = 0; i < 100; i++) {
      m.put(i,i);
    }
    assertEquals(99, m.get(99));
    assertEquals(100, m.size());
  }
}
