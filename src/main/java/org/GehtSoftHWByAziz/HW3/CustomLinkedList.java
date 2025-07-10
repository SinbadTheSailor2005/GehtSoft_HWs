package org.GehtSoftHWByAziz.HW3;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;


public class CustomLinkedList<T> implements List<T>, Deque<T> {
  private Node<T> first = null;
  private Node<T> last = null;
  private int counter = 0;

  public CustomLinkedList() {

  }
  @Override
  public CustomLinkedList<T> reversed() {
    var l = new CustomLinkedList<T>();
    Node<T> current = last;
    while(last != null) {
      l.add(last.data);
      last = last.previous;
    }
    return l;
  }

  @Override
  public boolean offerFirst(T t) {
    Node<T> nodeToAdd = new Node<>(t);
    counter++;
    first.previous = nodeToAdd;
    nodeToAdd.next = first;
    first = nodeToAdd;
    return true;
  }

  @Override
  public boolean offerLast(T t) {
    Node<T> nodeToAdd = new Node<>(t);
    counter++;
    last.next = nodeToAdd;
    nodeToAdd.previous = last;
    last = nodeToAdd;
    return true;
  }

  @Override
  public T pollFirst() {
    if (size() == 0) return null;
    T e = first.data;
    first.next.previous = null;
    first = first.next;
    counter--;
    return e;
  }

  @Override
  public T pollLast() {
    T e = last.data;
    last.previous.next = null;
    last = last.previous;
    counter--;
    return e;
  }

  @Override
  public T peekFirst() {
    if (first == null) return  null;
    return first.data;
  }

  @Override
  public T peekLast() {
    if (last == null) return null;
    return last.data;
  }

  private boolean removeNode(Node<T> n) {
    n.previous.next = n.next;
    n.next.previous = n.previous;
    return true;
  }
  @Override
  public boolean removeFirstOccurrence(Object o) {
    Node<T> current = first;
    while (current != null) {
      if (Objects.equals(current.data, o)) {
        removeNode(current);
        return true;
      }
      current = current.next;
    }
    return false;
  }

  @Override
  public boolean removeLastOccurrence(Object o) {
    Node<T> current = last;
    while (current != null) {
      if (Objects.equals(current.data, o)) {
        removeNode(current);
        return true;
      }
      current = current.previous;
    }
    return false;  }

  @Override
  public boolean offer(T t) {
    return add(t);
  }

  @Override
  public T remove() {
    if (size() == 0) {
      throw new NoSuchElementException();
    }
    return poll();
  }

  @Override
  public T poll() {
    return pollFirst();
  }

  @Override
  public T element() {
    if (size() == 0) throw new NoSuchElementException();
    return peek();
  }

  @Override
  public T peek() {
    return peekFirst();
  }

  @Override
  public void push(T t) {
    addFirst(t);
  }

  @Override
  public T pop() {
    return removeLast();
  }

  @Override
  public Iterator<T> descendingIterator() {
    return null;
  }

  @Override
  public int size() {

    return counter;
  }

  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  @Override
  public boolean contains(Object o) {
    Node<T> current = first;
    while (current != null) {
      if (Objects.equals(o, current.data)) return true;
      current = current.next;
    }
    return false;
  }

  @Override
  public Iterator<T> iterator() {
    return null;
  }

  @Override
  public void forEach(Consumer<? super T> action) {
    List.super.forEach(action);
  }

  @Override
  public Object[] toArray() {
    Object[] arr = new Object[size()];
    Node<T> current = first;
    int i = 0;
    while(current != null) {
      arr[i] = current.data;
      i ++;
      current = current.next;
    }
    return new Object[0];
  }

  @Override
  public <T1> T1[] toArray(T1[] t1s) {
    if (t1s.length > size()) return  (T1[])toArray();

    int i = 0;
    Node<T> current = first;
    while(current != null) {
      t1s[i] = (T1)current.data;
      i++;
      current = current.next;
    }
    return t1s;
  }

  @Override
  public <T1> T1[] toArray(IntFunction<T1[]> generator) {
    T1[] arr = generator.apply(size());
    int i = 0;
    Node<T> current = first;
    while (current != null) {
      arr[i] = (T1)current.data;
      i ++;
      current = current.next;
    }
    return arr;
  }

  @Override
  public boolean add(T t) {
    var nodeToAdd = new Node<T>(t);
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
    return true;  }

  @Override
  public boolean remove(Object o) {

    return removeFirstOccurrence(o);
  }

  @Override
  public boolean containsAll(Collection<?> collection) {
    for (var e : collection) {
      if (!contains(e)) return false;
    }
    return true;
  }

  @Override
  public boolean addAll(Collection<? extends T> collection) {
    for (var e : collection) {
      offer(e);
    }
    return true;
  }

  @Override
  public boolean addAll(int i, Collection<? extends T> collection) {
    for (var e : collection) {

      add(i++,e);
    }
    return true;
  }

  @Override
  public boolean removeAll(Collection<?> collection) {

    for (var e : collection) {
      remove(e);
    }
    return true;
  }

  @Override
  public boolean removeIf(Predicate<? super T> filter) {
    if (filter == null) throw new UnsupportedOperationException();
    Node<T> current = first;
    while (current != null) {
      if (filter.test(current.data)) {
        remove(current.data);
        return true;
      }
      current = current.next;
    }
    return false;
  }

  @Override
  public boolean retainAll(Collection<?> collection) {
    Node<T> current = first;
    boolean didChanged = false;
    while(current != null ){
      var t = current.next;
      if (!collection.contains(current.data)) {
        remove(current);
        didChanged = true;
      }
      current = t;
    }
    return didChanged;
  }

  @Override
  public void replaceAll(UnaryOperator<T> operator) {
    Node<T> current = first;
    while (current != null) {
      current.data = operator.apply(current.data);
      current = current.next;
    }
  }

  @Override
  public void sort(Comparator<? super T> c) {
    T[] a = this.toArray((T[]) new Object[size()]);
    Arrays.sort(a, c);
    int i  = 0;
    Node<T> current = first;
    while (current != null) {
      current.data = a[i];
      i++;
      current = current.next;
    }

  }

  @Override
  public void clear() {
  first = null;
  last = null;
  counter = 0;
  }

  @Override
  public T get(int i) {
    return getNode(i).data;
  }

  @Override
  public T set(int i, T t) {
    return getNode(i).data = t;
  }

  private Node<T> getNode(int i) {
    Objects.checkIndex(i, size());
    Node<T> current = first;
    for (int j = 0; j < i; j++) {
      current = current.next;
    }
    return current;
  }

  @Override
  public void add(int i, T t) {
    counter++;
    Objects.checkIndex(i, size());
    Node<T> insert = new Node<>(t);
    var n = getNode(i);
    if (n != null) {
      n.previous.next = insert;
      insert.previous = n.previous;
      insert.next = n;
      n.previous = insert;
    } else {
      add(t);
    }
  }

  @Override
  public T remove(int i) {
    Objects.checkIndex(i, size());
    Node<T> delete = getNode(i);
    delete.previous.next = delete.next;
    delete.next.previous = delete.previous;
    delete.next = null;
    delete.previous = null;
    counter--;
    return delete.data;
  }

  @Override
  public int indexOf(Object o) {
    Node<T> current = first;
    int i = 0;
    while (current != null) {
      if (Objects.equals(current.data, o)) return i;
      i++;
      current = current.next;
    }
    return -1;
  }

  @Override
  public int lastIndexOf(Object o) {
    int last_ind = -1;
    Node<T> current = first;
    int i = 0;
    while (current != null) {
      if (Objects.equals(o, current.data)) last_ind = i;
      i++;
    }
    return last_ind;
  }

  // Итераторы вроде необязательны
  @Override
  public ListIterator<T> listIterator() {
    return null;
  }

  @Override
  public ListIterator<T> listIterator(int i) {
    return null;
  }

  @Override
  public List<T> subList(int i1, int i2) {
    Objects.checkIndex(i1, size());
    Objects.checkIndex(i2, size() + 1);
    ArrayList<T>a = new ArrayList<>();
    for (int j = i1 ; j < i2; j ++) {
      a.add(getNode(j).data);
    }
    return a;
  }

  @Override
  public Spliterator<T> spliterator() {
    return List.super.spliterator();
  }

  @Override
  public Stream<T> stream() {
    return List.super.stream();
  }

  @Override
  public Stream<T> parallelStream() {
    return List.super.parallelStream();
  }

  @Override
  public void addFirst(T t) {
    offerFirst(t);
  }

  @Override
  public void addLast(T t) {
    offerLast(t);
  }

  @Override
  public T getFirst() {
    return first.data;
  }

  @Override
  public T getLast() {
    return last.data;
  }

  @Override
  public T removeFirst() {
    return pollFirst();
  }

  @Override
  public T removeLast() {
    return pollLast();
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
