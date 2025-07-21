package org.GehtSoftHWByAziz.HW3;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomLinkedListTest {

//    private CustomLinkedList<Integer> list;
//    private LinkedList<Integer> linkedList;

//    @BeforeEach
//    void setUp(List<Integer> list) {
//      list = new CustomLinkedList<Integer>();
//      linkedList = new LinkedList<Integer>();
//
//    }
//
//    @AfterEach
//    void tearDown(List<Integer> list) {
//      linkedList.clear();
//      list.clear();
//    }


  static List<List<Integer>>listImplementations() {
    return List.of(new CustomLinkedList<>(), new LinkedList<>());
  }

    @ParameterizedTest
    @MethodSource("listImplementations")
    void testSize(List<Integer> list) {
      for (int i = 0; i < 100; i++) {
        list.add(i);
      }
      assertEquals(100, list.size());
    }



  @ParameterizedTest
  @MethodSource("listImplementations")
    void testAdd(List<Integer> list) {
      list.add(0);
      list.add(1);
      list.add(2);
      list.add(3);

      list.add(1, 100);
      assertEquals(0, list.get(0));
      assertEquals(100, list.get(1));
      assertEquals(1, list.get(2));
      assertEquals(2, list.get(3));
      assertEquals(3, list.get(4));
      System.out.println(list.size());
    }

     @ParameterizedTest
  @MethodSource("listImplementations")
    void testAddByIndexToEmptyList(List<Integer> list) {
      System.out.println(list.size());
      list.add(0, 100);
      System.out.println(list.size());
      assertEquals(100, list.get(0));
      assertEquals(1, list.size());
    }

     @ParameterizedTest
  @MethodSource("listImplementations")
    void testRemove(List<Integer> list) {
      for (int i = 0; i < 100; i++) {
        list.add(i);
      }

      for (int i = 0; i < 100; i++) {
        list.remove(0);
      }
      assertEquals(0, list.size());
      assertThrows(RuntimeException.class, () -> list.get(0));
    }

     @ParameterizedTest
  @MethodSource("listImplementations")
    void testIsEmpty(List<Integer> list) {
      for (int i = 0; i < 100; i++) {
        list.add(i);
      }

      for (int i = 0; i < 100; i++) {
        list.remove(0);
      }
      assertTrue(list.isEmpty());
    }

     @ParameterizedTest
  @MethodSource("listImplementations")
    void testContains(List<Integer> list) {
      for (int i = 0; i < 100; i++) {
        list.add(i);
      }
      assertTrue(list.contains(99));
    }

     @ParameterizedTest
  @MethodSource("listImplementations")
    void testClear(List<Integer> list) {
      for (int i = 0; i < 100; i++) {
        list.add(i);
      }
      list.clear();

      assertEquals(0, list.size());
      assertThrows(RuntimeException.class, () -> list.get(0));
    }

     @ParameterizedTest
  @MethodSource("listImplementations")
    void testSet(List<Integer> list) {
      assertThrows(RuntimeException.class, () -> list.set(0, 100));
      list.add(0, 100);
      assertEquals(100, list.get(0));
    }

     @ParameterizedTest
  @MethodSource("listImplementations")
    void edgeCases(List<Integer> list) {
      assertEquals(0,list.size());
      for (int i = 0; i < 100; i++) {
        list.add(i);
      }
      assertThrows(RuntimeException.class, () -> list.get(100));

    }

     @ParameterizedTest
  @MethodSource("listImplementations")
    void testToArray(List<Integer> list) {
      var temp = new Integer[10];
      for (int i = 0; i < 100; i++) {
        list.add(i);
      }
      temp = list.toArray(temp);
      for (int i = 0; i < temp.length; i++) {
        temp[i] = temp[i] + 100;
      }    for (int i = 0 ; i < 100 ; i ++ ) {
        assertNotEquals(temp[i], list.get(i));
      }
      assertEquals(100,temp.length);
    }

     @ParameterizedTest
  @MethodSource("listImplementations")
    void testContainsAll(List<Integer> list) {

      var temp = new ArrayList<>(List.of(1,2,3));
      for (int i = 0; i < 100; i++) {
        list.add(i);
      }
      assertTrue(list.containsAll(temp));
    }

     @ParameterizedTest
  @MethodSource("listImplementations")
    void testAddAll(List<Integer> list) {

      var temp = new ArrayList<>(List.of(0,1,2));
      list.addAll(temp);
      for (int i = 0 ; i < 3; i ++) {
        assertEquals(i, list.get(i));
      }

    }

     @ParameterizedTest
  @MethodSource("listImplementations")
    void removeAll(List<Integer> list) {

      var temp = new ArrayList<>(List.of(0,1,2));
      list.addAll(List.of(0,1,2));
      list.removeAll(temp);
      assertEquals(0,list.size());
      assertThrows(RuntimeException.class, () -> list.get(0));
    }

     @ParameterizedTest
  @MethodSource("listImplementations")
    void testIndexOf(List<Integer> list) {
      list.add(100);
      assertEquals(0, list.indexOf(100));
    }
     @ParameterizedTest
  @MethodSource("listImplementations")
    void testLastIndexOf(List<Integer> list) {
      list.add(100);
      list.add(100);
      list.add(100);
      list.add(100);

      assertEquals(3, list.lastIndexOf(100));
    }




     @ParameterizedTest
  @MethodSource("listImplementations")
    void testAddGetDeletion(List<Integer> list) {
      for (int i = 0; i < 100; i++) {
        list.add(i);
      }
      assertEquals(100, list.size());

      for (int i = 0; i < 100; i++) {
        assertEquals(i,list.get(i));
      }
      list.clear();

      assertEquals(0, list.size());
    }


  }
