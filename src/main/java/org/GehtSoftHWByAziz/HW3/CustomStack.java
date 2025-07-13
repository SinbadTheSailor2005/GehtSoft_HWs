package org.GehtSoftHWByAziz.HW3;

import java.util.Stack;

public class CustomStack<T>  {
  private CustomLinkedList <T> l = new CustomLinkedList<>();

  public void push(T e){
   l.push(e);
  }
  public T pop () {
  return  l.pop();
  }
  public T peek() {
  return l.peek();
  }

  public boolean isEmpty() {
  return l.isEmpty();
  }

  public int search (T e) {
   int i = l.lastIndexOf(e);
   return i >=0 ? l.size() - i : -1;
  }
}
