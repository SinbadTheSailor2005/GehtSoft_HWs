package org.GehtSoftHWByAziz.HW4;

import java.util.*;

public class CustomHashMap <K,V>implements Map<K,V> {
  final private int INIT_TABLE_SIZE = 10;
  final private int GROW_COEFFICIENT = 2;
  private  Collection <V>values;
  private Set<K> keys;
  Node<K,V>[] table;
  int count;

  public CustomHashMap() {
    this.table =  (Node<K,V> [])new Node[INIT_TABLE_SIZE];
     this.count = -1;
     this.keys = new HashSet<>();
     this.values = new ArrayList<>();
  }

  @Override
  public int size() {
    return this.count +1;
  }

  @Override
  public boolean isEmpty() {
    return this.size() == 0;
  }

  @Override
  public boolean containsKey(Object o) {
    int indx = Math.abs(o.hashCode()) % this.table.length;
    return this.table[indx] != null;
  }

  @Override
  public boolean containsValue(Object o) {
    for (int i = 0 ; i < this.table.length; i ++ ) {
      var node = table[i];
        while (node!=null) {
          if (node.value.equals(o)) return true;
          node = node.next;
        }
      }
    return false;
  }

  @Override
  public V get(Object o) {
    int ind = Math.abs(o.hashCode()) % table.length;
    var node = table[ind];
    while (node != null) {
      if (node.key.equals(o)) return node.value;
      node = node.next;
    }
    return null;
  }

  @Override
  public V put(K k, V v) {
    count ++;
    this.keys.add(k);
    this.values.add(v);
    if (needGrow()) grow();
    int ind = Math.abs(k.hashCode()) % table.length;
    var node = table[ind];
    if (node == null) {
      table[ind] = new Node<>(k,v);
      return v;
    }
    while (node.next != null) {
      if (node.key.equals(k)) {
        node.value = v;
        return v;
      }
      node = node.next;
    }
    if (node.key.equals(k)) {
      node.value = v;
      return v;
    }
    node.next = new Node<>(k,v);
    return null;
  }

  private boolean grow() {
    try {


      this.table = Arrays.copyOf(
              this.table,
              (this.table.length * GROW_COEFFICIENT)
      );
      return true;
    }catch (Exception e) {
      System.out.println(e);
      return  false;
    }
  }

  private boolean needGrow() {
    return (double)count / (double)table.length >= 0.75;
  }

  @Override
  public V remove(Object key) {
    count --;
    this.keys.remove(key);
    int ind = Math.abs(key.hashCode()) % table.length;
    if (table[ind] == null) return null;
    if (table[ind].key.equals(key)) {
      var v = table[ind].value;
      table[ind] = table[ind].next;
      return v;
    }
    var node = table[ind];
    while (node.next != null) {
      if (node.next.key.equals(key)) {
        var v = node.next.value;
        node.next = node.next.next;
        return v;
      }
    }
    return null;
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> map) {
    for (var entry : map.entrySet()) {
      this.put(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public void clear() {
    for (int i = 0 ; i < table.length; i ++) {
      table[i] = null;
      this.values.clear();
      this.keys.clear();
    }
    count = -1;
  }

  @Override
  public Set<K> keySet() {
    return this.keys;
  }

  @Override
  public Collection<V> values() {
    return this.values;
  }

  @Override
  public Set<Entry<K, V>> entrySet() {
    var entries = new HashSet<Entry<K,V>>();
    for (int i = 0 ; i < table.length; i ++) {
      var node =  table [i];
      while (node != null) {
        entries.add(node);
        node = node.next;
      }

    }
    return entries;
  }


  static class Node <K,V> implements Entry<K,V> {
    K key;
    V value;
    Node<K, V> next;

    public Node( K key, V value) {
      this.key = key;
      this.value = value;
      this.next = null;
    }

    @Override
    public K getKey() {
      return key;
    }

    @Override
    public V getValue() {
      return value;
    }

    @Override
    public V setValue(V v) {
      return this.value = v;
    }
  }
}
