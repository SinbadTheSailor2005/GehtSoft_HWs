package org.GehtSoftHWByAziz.HW3;

import java.util.*;


/*
  There is a conflict in reverse() method in Java 21:
  List returns List<>, Deque returns Deque, so current
  implementation do not rely on interfaces, but still implement all methods
 */
public class ewq<T>  {
  private Node<T> first = null;
  private Node<T> last = null;
  private int counter = 0;
  public List<T> reversed() {
    List<T> r = new ArrayList<>();
    Node<T> current = last;
    while (current != null) {
      r.add(current.data);
      current = current.previous;
    }


    return r;
  }

  public boolean offerLast(T t) {
    Node<T> nodeToAdd = new Node<>(t);
    counter++;
    last.next = nodeToAdd;
    nodeToAdd.previous = last;
    last = nodeToAdd;
    return true;
  }

  
  public boolean offerFirst(T t) {
    Node<T> nodeToAdd= new Node<>(t);
    counter++;
    first.previous = nodeToAdd;
    nodeToAdd.next = first;
    first = nodeToAdd;
    return true;
  }

  
  public T pollFirst() {
    T e = first.data;
    first.next.previous = null;
    first = first.next;
    counter --;
    return  e;

  }

  
  public T pollLast() {
    T e = last.data;
    last.previous.next = null;
    last = last.previous;
    counter --;
    return e;
  }

  
  public T peekFirst() {
    return first.data;
  }

  
  public T peekLast() {
    return last.data;
  }

  
  public boolean removeFirstOccurrence(Object o) {
    return false;
  }

  
  public boolean removeLastOccurrence(Object o) {
    return false;
  }

  
  public boolean offer(T t) {
    return false;
  }

  
  public T remove() {
    return ;
  }

  
  public T poll() {
    return null;
  }

  
  public T element() {
    return null;
  }

  
  public T peek() {
    return null;
  }

  
  public void push(T t) {

  }

  
  public T pop() {
    return null;
  }

  
  public Iterator<T> descendingIterator() {
    return null;
  }

  
  public int size() {
    return 0;
  }

  
  public boolean isEmpty() {
    return false;
  }

  
  public boolean contains(Object o) {
    return false;
  }

  
  public Iterator<T> iterator() {
    return null;
  }

  
  public Object[] toArray() {
    return new Object[0];
  }

  
  public <T1> T1[] toArray(T1[] t1s) {
    return null;
  }


  private Node<T> getNode(int i) {
    Objects.checkIndex(i, size());
    Node<T> current = first;
    for (int j =0; j < i; j ++) {
      current = current.next;
    }
    return current;
  }
  public boolean add(T data) {
    var nodeToAdd = new Node<T>(data);
    counter ++;
    if (last != null) {
      last.next = nodeToAdd;
      nodeToAdd.previous = last;
      last = nodeToAdd;

    }
    else {
      last = nodeToAdd;
      first = nodeToAdd;
    }
    return true;
  }


  
  public boolean remove(Object o) {
    Node<T> current = first;
    while (current != null) {
      if (Objects.equals(o, current.data)) {
        remove()
      }
    }
    return false;
  }

  
  public boolean containsAll(Collection<?> collection) {
    return false;
  }

  
  public boolean addAll(Collection<? extends T> collection) {
    return false;
  }

  
  public boolean addAll(int i, Collection<? extends T> collection) {
    return false;
  }

  
  public boolean removeAll(Collection<?> collection) {
    return false;
  }

  
  public boolean retainAll(Collection<?> collection) {
    return false;
  }

  
  public void clear() {

  }

  
  public T get(int i) {
    return null;
  }

  
  public T set(int i, T t) {
    return null;
  }


  public boolean add (int i, T data) {
    counter++;
    Objects.checkIndex(i, size());
    Node<T> insert = new Node<>(data);
    var n = getNode(i);
    if (n != null) {
      n.previous.next = insert;
      insert.previous = n.previous;
      insert.next = n;
      n.previous = insert;
    } else {
      add(data);
    }
    return true;
  }

  
  public T remove(int i) {
    Objects.checkIndex(i, size());
    Node<T> delete = getNode(i);
    delete.previous.next = delete.next;
    delete.next.previous = delete.previous;
    delete.next=null;
    delete.previous=null;
    return  delete.data;
  }

  
  public int indexOf(Object o) {
    return 0;
  }

  
  public int lastIndexOf(Object o) {
    return 0;
  }


  public ListIterator<T> listIterator() {
    return null;
  }


  public ListIterator<T> listIterator(int i) {
    return null;
  }


  public List<T> subList(int i, int i1) {
    return List.of();
  }
  private static class Node<T> {
    private Node(T data) {
      this.data = data;
    }

    Node<T> next = null;
    Node<T> previous = null;
    T data;
  }
}
