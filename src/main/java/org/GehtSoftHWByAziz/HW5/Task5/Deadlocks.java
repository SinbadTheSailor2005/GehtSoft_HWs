package org.GehtSoftHWByAziz.HW5.Task5;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Deadlocks {
  static Lock lock1 = new ReentrantLock();
  static Lock lock2 = new ReentrantLock();
  public static void main (String [] args) throws InterruptedException {
  deadlock3();
  }

  /**
   * deadlock happens because
   * first thread aquires lock1 and then tries to acquire lock2
   * which is already held by second thread.
   * thread 1 waits for lock2, thread 2 waits for lock1
   * Deadlock occurs
   * @throws InterruptedException
   */
  public static void deadlock1() throws InterruptedException {
    Thread t1 = new Thread( () -> {
      lock1.lock();
      lock2.lock();
    });
    Thread t2 = new Thread( () -> {
      lock2.lock();
      lock1.lock();
    });
    System.out.println("Starting thread 1");
    t1.start();
    System.out.println("Starting thread 2");
    t2.start();
    t1.join();
    t2.join();
    System.out.println("Never reached...");
  }

  /**
   * deadlock happens because
   * first thread aquires lock1 and then throws an exception, but it does not release lock1
   * second thread tries to acquire lock1, but it is already held by first thread
   * Deadlock occurs
   * FIX: add finally block to guarantee release lock
   * @throws InterruptedException
   */
  public static void deadlock2() throws InterruptedException {
    Thread t1 = new Thread( ()->{
      lock1.lock();
      throw new RuntimeException("Simulating exception in thread 1, but " +
              "forget to release lock1");
    });
    Thread t2 = new Thread(() -> {
      lock1.lock();
    });
    System.out.println("Starting thread 1");
    t1.start();
    System.out.println("Starting thread 2");
    t2.start();

    t1.join();
    t2.join();
    System.out.println("Never reached...");

  }

  /**
   * the same story as in deadlock1
   * thread 1 acquires obj1 and then tries to acquire obj2
   * thread 2 acquires obj2 and then tries to acquire obj1
   * Deadlock occurs
   * @throws InterruptedException
   */
  public static  void deadlock3 () throws InterruptedException {
    Object obj1 = new Object();
    Object obj2 = new Object();
    Thread t1 = new Thread(() -> {
      synchronized (obj1) {
        try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        synchronized (obj2){
          try {
            Thread.sleep(2000);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
      }

    });

    Thread t2 = new Thread(() -> {
      synchronized (obj2) {
        try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        synchronized (obj1){
          try {
            Thread.sleep(2000);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
      }
    });
    System.out.println("Starting thread 1");
    t1.start();
    System.out.println("Starting thread 2");
    t2.start();
    t1.join();
    t2.join();
    System.out.println("Never reached...");
  }
}
