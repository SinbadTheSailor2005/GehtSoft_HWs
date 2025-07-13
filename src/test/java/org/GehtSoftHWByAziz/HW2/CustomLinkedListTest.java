package org.GehtSoftHWByAziz.HW2;

import org.GehtSoftHWByAziz.HW1.CustomList;
import org.GehtSoftHWByAziz.HW3.CustomLinkedList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomLinkedListTest {

    private CustomLinkedList<Integer> myList;
    private LinkedList<Integer> linkedList;

    @BeforeEach
    void setUp() {
      myList = new CustomLinkedList<Integer>();
      linkedList = new LinkedList<Integer>();

    }

    @AfterEach
    void tearDown() {
      linkedList.clear();
      myList.clear();
    }




    @Test
    void testSize() {
      for (int i = 0; i < 100; i++) {
        myList.add(i);
      }
      assertEquals(100, myList.size());
    }



    @Test
    void testAdd() {
      myList.add(0);
      myList.add(1);
      myList.add(2);
      myList.add(3);

      myList.add(1, 100);
      assertEquals(0, myList.get(0));
      assertEquals(100, myList.get(1));
      assertEquals(1, myList.get(2));
      assertEquals(2, myList.get(3));
      assertEquals(3, myList.get(4));
      System.out.println(myList.size());
    }

    @Test
    void testAddByIndexToEmptyList() {
      System.out.println(myList.size());
      myList.add(0, 100);
      System.out.println(myList.size());
      assertEquals(100, myList.get(0));
      assertEquals(1, myList.size());
    }

    @Test
    void testRemove() {
      for (int i = 0; i < 100; i++) {
        myList.add(i);
      }

      for (int i = 0; i < 100; i++) {
        myList.remove(0);
      }
      assertEquals(0, myList.size());
      assertThrows(RuntimeException.class, () -> myList.get(0));
    }

    @Test
    void testIsEmpty() {
      for (int i = 0; i < 100; i++) {
        myList.add(i);
      }

      for (int i = 0; i < 100; i++) {
        myList.remove(0);
      }
      assertTrue(myList.isEmpty());
    }

    @Test
    void testContains() {
      for (int i = 0; i < 100; i++) {
        myList.add(i);
      }
      assertTrue(myList.contains(99));
    }

    @Test
    void testClear() {
      for (int i = 0; i < 100; i++) {
        myList.add(i);
      }
      myList.clear();

      assertEquals(0, myList.size());
      assertThrows(RuntimeException.class, () -> myList.get(0));
    }

    @Test
    void testSet() {
      assertThrows(RuntimeException.class, () -> myList.set(0, 100));
      myList.add(0, 100);
      assertEquals(100, myList.get(0));
    }

    @Test
    void edgeCases() {
      assertEquals(0,myList.size());
      for (int i = 0; i < 100; i++) {
        myList.add(i);
      }
      assertThrows(RuntimeException.class, () -> myList.get(100));

    }

    @Test
    void testToArray() {
      var temp = new Integer[10];
      for (int i = 0; i < 100; i++) {
        myList.add(i);
      }
      temp = myList.toArray(temp);
      for (int i = 0; i < temp.length; i++) {
        temp[i] = temp[i] + 100;
      }    for (int i = 0 ; i < 100 ; i ++ ) {
        assertNotEquals(temp[i], myList.get(i));
      }
      assertEquals(100,temp.length);
    }

    @Test
    void testContainsAll() {

      var temp = new ArrayList<>(List.of(1,2,3));
      for (int i = 0; i < 100; i++) {
        myList.add(i);
      }
      assertTrue(myList.containsAll(temp));
    }

    @Test
    void testAddAll() {

      var temp = new ArrayList<>(List.of(0,1,2));
      myList.addAll(temp);
      for (int i = 0 ; i < 3; i ++) {
        assertEquals(i, myList.get(i));
      }

    }

    @Test
    void removeAll() {

      var temp = new ArrayList<>(List.of(0,1,2));
      myList.addAll(List.of(0,1,2));
      myList.removeAll(temp);
      assertEquals(0,myList.size());
      assertThrows(RuntimeException.class, () -> myList.get(0));
    }

    @Test
    void testIndexOf() {
      myList.add(100);
      assertEquals(0, myList.indexOf(100));
    }
    @Test
    void testLastIndexOf() {
      myList.add(100);
      myList.add(100);
      myList.add(100);
      myList.add(100);

      assertEquals(3, myList.lastIndexOf(100));
    }




    @Test
    void testArrayListAndMyListAndCheckSublist() {
      for (int i = 0; i < 100; i++) {
        myList.add(i);
        linkedList.add(i);
      }
      assertEquals(myList.size(), linkedList.size());

      for (int i = 0; i < 100; i++) {
        assertEquals(myList.get(i), linkedList.get(i));
      }
      var temp  = myList.subList(0,100);
      myList.removeAll(linkedList);
      linkedList.removeAll(temp);

      assertEquals(myList.size(),linkedList.size());
      assertEquals(0, myList.size());
    }


  }
