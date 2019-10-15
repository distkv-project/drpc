package org.drpc.server;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentLinkedSPSCQueue<T> implements SPSCQueue<T> {

  private ConcurrentLinkedQueue<T> queue;

  public void append(T item) {
    queue.add(item);
  }

  public T pop() {
    return queue.poll();
  }

  public int size() {
    return queue.size();
  }

  public boolean isEmpty() {
    return queue.isEmpty();
  }

  public boolean isFull() {
    return false;
  }

}
