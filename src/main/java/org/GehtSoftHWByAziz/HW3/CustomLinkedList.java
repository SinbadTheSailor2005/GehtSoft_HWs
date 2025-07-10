package org.GehtSoftHWByAziz.HW3;

import org.GehtSoftHWByAziz.HW1.CustomList;

import java.util.LinkedList;
import java.util.Objects;


public class CustomLinkedList<T> {
  private Node<T> first = null;
  private Node<T> last = null;
  private int counter = 0;

  public int size () {
    return  this.counter;
  }
  public void add(T data) {
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
  }


  private Node<T> getNode(int i) {
    Objects.checkIndex(i, size());
    Node<T> current = first;
    for (int j =0; j < i; j ++) {
      current = current.next;
    }
    return current;
  }

  public void add (int i, T data) {
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
  }

  public void remove (int i) {
    Objects.checkIndex(i, size());
    Node<T> delete = getNode(i);
    delete.previous.next = delete.next;
    delete.next.previous = delete.previous;
    delete.next=null;
    delete.previous=null;
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
