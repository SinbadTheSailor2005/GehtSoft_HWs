package org.GehtSoftHWByAziz;

import java.util.*;

public class CustomList<T> implements List<T> {
  private final int GROW_COEFFICIENT = 2;
  private final int INIT_LENGHT = 3;
  private Object[] data;
  private int headElement;

  public CustomList() {
    this.headElement = -1;
    try {


      this.data = new Object[INIT_LENGHT];
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  public int lenght() {
    return this.data.length;
  }
  @Override
  public int size() {

    return this.headElement + 1; // +1 since headElement contains index of
    // last element
  }

  @Override
  public boolean isEmpty() {

    return this.size() == 0;
  }

  @Override
  public boolean contains(Object o) {
    for (int i = 0; i < this.size(); i++) {
      if (Objects.equals(o, this.data[i])) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Iterator<T> iterator() {
    return new CustomListIterator();
  }

  @Override
  public Object[] toArray() {
    Object[] clone = new Object[this.size()];
    System.arraycopy(this.data, 0, clone, 0, this.size());
    return clone;
  }
  @SuppressWarnings("unchecked")
  @Override
  public <T1> T1[] toArray(T1[] t1s) {
    if (this.size() > t1s.length) {
      return (T1[]) Arrays.copyOf(this.data, this.size(), t1s.getClass());
    }
    System.arraycopy(this.data, 0, t1s, 0, this.size());
    return t1s;
  }


  // i-th pos will be free
  // i.e. i = 1 => [1,2,3] -> [1,2,2,3]
  public void shiftRight(int i) {
    Object previousElement = this.data[i];
    Object temp;
    for (int j = i + 1; j < size(); j++) {
      temp = this.data[j];
      this.data[j] = previousElement;
      previousElement = temp;
    }
  }


  private boolean isSpaceEnough() {
    return this.size() < this.data.length;
  }


  /*
  Warning: add method with index insert properly
           if index is in [0; headElement]

   */
  private boolean isOutOfBoundary(int i) {
    return (i < 0 || i > size());
  }

  @Override
  public void add(int i, T t) {

    if (!isSpaceEnough()) {
      this.grow();
    }
    if (isOutOfBoundary(i)) {
      throw new RuntimeException("Index out of boundary");
    }

    this.headElement++;
    shiftRight(i);
    this.data[i] = t;
  }

  @Override
  public boolean add(T t) {
    add(headElement + 1, t);
    return true;
  }

  @Override
  public boolean remove(Object o) {
    for (int i = 0; i < this.size(); i++) {
      if (Objects.equals(this.data[i], o)) {
        this.remove(i);

        return true;
      }
    }
    return false;
  }

  @Override
  public boolean containsAll(Collection<?> collection) {
    for (Object o : collection) {
      if (!contains(o)) return false;
    }
    return true;
  }

  @Override
  public boolean addAll(Collection<? extends T> collection) {
    for (T t : collection) {
      add(t);
    }
    return false;
  }

  @Override
  public boolean addAll(int i, Collection<? extends T> collection) {
    for (T t : collection) {
      add(i, t);
      i++;
    }
    return false;
  }

  @Override
  public boolean removeAll(Collection<?> collection) {
//    for (int i  = 0; i < collection.size(); i ++) {
//      remove(collection.);
//    }
    boolean didChanged = false;
    for (Object element : collection) {
      if (remove(element)) didChanged = true;
    }
    return didChanged;
  }

  @Override
  public boolean retainAll(Collection<?> collection) {
    boolean didChanged = false;
    for (Object o : this.data) {
      if (!collection.contains(o)) {
        remove(o);
        didChanged = true;
      }
    }
    return didChanged;
  }

  @Override
  public void clear() {
    this.headElement = -1; // TODO: сделать unit test
  }

  /*
  WARNING: if we try to access element that does not exit aka i != [0, size()]
  position, the exception will pop throw up
   */

  @SuppressWarnings("unchecked")
  @Override
  public T get(int i) {
    // Warning: throws IndexOutOfBoundsException exception
    Objects.checkIndex(i, this.size());
    return (T) this.data[i];
  }

  @Override
  public T set(int i, T t) {
    // Warning: throws IndexOutOfBoundsException exception
    Objects.checkIndex(i, size());
    this.data[i] = t;
    return t;
  }


  private boolean grow() {
    try {


      this.data = Arrays.copyOf(this.data, this.data.length * GROW_COEFFICIENT);
      return true;
    } catch (Exception e) {
      return false;
    }
  }


  // i -th pos will be collapse
  // i.e. i = 1 =>  [1,2,3] -> [1,3]
  public void shiftLeft(int i) {
    if (i == this.headElement) return;
    for (int j = i + 1; j < size(); j++) {
      this.data[j - 1] = this.data[j];
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public T remove(int i) {
    Object removedElement = this.data[i];
    shiftLeft(i);
    headElement--;
    return (T) removedElement;
  }

  @Override
  public int indexOf(Object o) {
    for (int i = 0; i < this.size(); i++) {
      if (Objects.equals(this.data[i], o)) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public int lastIndexOf(Object o) {
    int lastIndex = -1;
    for (int i = 0; i < this.size(); i++) {
      if (Objects.equals(o, this.data[i])) {
        lastIndex = i;
      }
    }
    return lastIndex;
  }

  @Override
  public ListIterator<T> listIterator() {
    return new CustomListIterator();
  }

  @Override
  public ListIterator<T> listIterator(int i) {
    var it = new CustomListIterator();
    it.currentPos = i;
    return it;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<T> subList(int i1, int i2) {
    // Warning: throws IndexOutOfBoundsException exception
    Objects.checkIndex(i1, size());
    Objects.checkIndex(i2, size() + 1); // i2 is exclusive
    List<T> subCollection = new ArrayList<>(i2 - i1 + 1);
    for (int i = i1; i < i2; i++) {
      subCollection.add((T)data[i]);
    }
    return subCollection;
  }

  // hash function was taken from ArrayList implementation
  public int hashCode() {
    int hash = 1;
    for (int i = 0 ; i < this.size(); i ++) {


      Object e = this.data[i];
      hash = 31 * hash + (e == null ? 0 : e.hashCode());
    }
    return  hash;
  }
  public boolean equals (Object o) {
    if (this == o) return true;
    else if (!(o instanceof List<?> list)) return false;
    else {
      if (list.size() != this.size()) return  false;
      for (int i = 0 ; i < this.size(); i ++){
        if (list.get(i) != this.data[i]) return false;
      }
      return true;
    }

  }

  private class CustomListIterator implements ListIterator<T> {

    private int currentPos = 0;
    private boolean canRemove = false;
    private boolean canSet = false;

    @Override
    public boolean hasNext() {

      return this.currentPos < size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T next() {
      if (!hasNext()) {
        throw new RuntimeException("Out of array boundary");
      }
      canRemove = true;
      canSet = true;
      return (T) data[currentPos++];
    }

    @Override
    public boolean hasPrevious() {
      return this.currentPos > 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T previous() {
      // Warning: if currentPos -1 < 0 ->
      // throws exception
      canRemove = true;
      canSet = true;
      return (T) data[--currentPos];
    }

    @Override
    public int nextIndex() {
      return currentPos;
    }

    @Override
    public int previousIndex() {
      if (currentPos == 0) return -1;
      return currentPos - 1;
    }

    @Override
    public void remove() {


      if (canRemove) CustomList.this.remove(currentPos);
      else {
        throw new RuntimeException("Cant use remove() method in Iterator");
      }
      canSet = false;
      canRemove = false;
    }

    @Override
    public void set(T t) {
      if (canSet) CustomList.this.set(currentPos, t);
      else throw new RuntimeException("Cant use set() method in Iterator");
    }

    @Override
    public void add(T t) {
      CustomList.this.add(currentPos - 1, t);
      canRemove = false;
      canSet = false;
    }
  }


}
