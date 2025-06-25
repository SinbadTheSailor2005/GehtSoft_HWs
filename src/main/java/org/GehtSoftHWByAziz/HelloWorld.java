package org.GehtSoftHWByAziz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelloWorld {
  public static void main (String [] args) {

    ArrayList<Integer> l = new ArrayList<>();
    l.add(1);
    l.add(2);
    l.add(3);
    ArrayList<Integer>l2 = new ArrayList<>();
    l2.add(5);
    l2.add(6);

    l.add(3,33);

    System.out.println(l);
    l.addAll(3,l2);
    System.out.println(l.containsAll(l2));
    System.out.println(l);
    l.retainAll(List.of(1,2,3,4,5));
    l.subList()
    System.out.println(l);
  }
}
