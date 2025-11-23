package org.GehtSoftHWByAziz.HW3;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class CustomQueue<T> implements Queue <T> {
  private CustomLinkedList<T> l = new CustomLinkedList<T>();
  @Override
  public int size() {
    return l.size();
  }

  @Override
  public boolean isEmpty() {

    return l.isEmpty();
  }

  @Override
  public boolean contains(Object o) {

    return l.contains(o);
  }

  @Override
  public Iterator iterator() {
    return null;
  }

  @Override
  public Object[] toArray() {
    return l.toArray();
  }

  @Override
  public Object[] toArray(Object[] objects) {
    return l.toArray(objects);
  }

  @Override
  public boolean add(T o) {
    return l.add((T)o);
  }

  @Override
  public boolean remove(Object o) {
    return l.remove((T)o);
  }

  @Override
  public boolean addAll(Collection collection) {
    return  l.addAll(collection);
  }

  @Override
  public void clear() {
    l.clear();
  }

  @Override
  public boolean retainAll(Collection collection) {
    return l.retainAll(collection);
  }

  @Override
  public boolean removeAll(Collection collection) {
    l.retainAll(collection);
    return false;
  }

  @Override
  public boolean containsAll(Collection collection) {
    return l.contains(collection);
  }

  @Override
  public boolean offer(Object o) {
    return l.offer((T)o);
  }

  @Override
  public T remove() {
    return l.remove();
  }

  @Override
  public T poll() {
    return l.poll();
  }

  @Override
  public T element() {
    return l.element();
  }

  @Override
  public T peek() {
    return l.peek();
  }
}
