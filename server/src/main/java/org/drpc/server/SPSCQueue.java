package org.drpc.server;

public interface SPSCQueue<T> {

  void append(T item);

  T pop();

  int size();

  boolean isEmpty();

  boolean isFull();
}
