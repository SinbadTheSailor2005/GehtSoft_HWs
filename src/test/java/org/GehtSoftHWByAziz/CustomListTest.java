package org.GehtSoftHWByAziz;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CustomListTest {
  private ArrayList<Integer> arrList;
  private CustomList<Integer> myList;

  @BeforeEach
  void setUp() {
    arrList = new ArrayList<Integer>();
    myList = new CustomList<Integer>();

  }

  @AfterEach
  void tearDown() {
    arrList.clear();
    myList.clear();
  }


  @Test
  void testGrow() {
    for (int i = 0; i < 4; i++) {
      myList.add(i);
    }
    assertEquals(4, myList.size());
    assertEquals(6, myList.lenght());
  }

  @Test
  void testSize() {
    for (int i = 0; i < 100; i++) {
      myList.add(i);
    }
    assertEquals(100, myList.size());
  }

  @Test
  void testInitConfiguration() {
    assertEquals(3, myList.lenght());
    assertEquals(0, myList.size());
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
  }

  @Test
  void testAddByIndexToEmptyList() {
    myList.add(0, 100);
    assertEquals(100, myList.get(0));
    assertEquals(1, myList.size());
  }

  @Test
  void testRemove() {
    for (int i = 0; i < 100; i++) {
      myList.add(i);
    }

    for (int i = 0; i < 100; i++) {
      myList.remove(i);
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
      myList.remove(i);
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
  void testIteratorAdd() {
    var it = myList.iterator();

    for (int i = 0; i < 100; i++) {
      myList.add(i);
    }
    int i = 0;
    while (it.hasNext()) {
      var element = it.next();
      assertEquals(myList.get(i), element);
      i++;
    }
    assertEquals(100, i);
  }

  @Test
  void testListIteratorAdd() {
    var it = myList.listIterator();

    for (int i = 0; i < 100; i++) {
      myList.add(i);
    }
    int i = 0;
    while (it.hasNext()) {
      var element = it.next();
      assertEquals(myList.get(i), element);
      i++;
    }
    assertEquals(100, i);
    while (it.hasPrevious()) {
      i--;
      var element = it.previous();
      assertEquals(element, myList.get(i));
    }
    assertEquals(0, i);
  }

  @Test
  void testIteratorRemove() {
    var it = myList.listIterator(100);
    for (int i = 0; i < 100; i++) {
      myList.add(i);
    }
    int i = 100;
    while (it.hasPrevious()) {
      it.previous();
      it.remove();
    }
    assertEquals(0, myList.size());
  }

  @Test
  void testIteratorSet() {
    var it = myList.iterator();
    for (int i = 0; i < 100; i++) {
      myList.add(i); // to increase size to 100
    }
    for (int i = 0; i < 100; i++) {
      myList.set(i, 11);
    }
    for (var i : myList) {
      assertEquals(11, i);
    }
    assertEquals(100, myList.size());
  }

  @Test
  void testArrayListAndMyListAndCheckSublist() {
    for (int i = 0; i < 100; i++) {
      myList.add(i);
      arrList.add(i);
    }
    assertEquals(myList.size(), arrList.size());

    for (int i = 0; i < 100; i++) {
    assertEquals(myList.get(i), arrList.get(i));
    }
    var temp  = myList.subList(0,100);
    myList.removeAll(arrList);
    arrList.removeAll(temp);

    assertEquals(myList.size(),arrList.size());
    assertEquals(0, myList.size());
  }

  @Test
  void testEqualsAndHash() {

    for (int i = 0; i < 100; i++) {
      myList.add(i);
      arrList.add(i);
    }
    assertEquals(myList, arrList);
    assertEquals(myList.hashCode(),arrList.hashCode());
  }
}
