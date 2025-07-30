package org.GehtSoftHWByAziz.HW5.Task3;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Client {

  static Random random = new Random();
  static final Lock lock = new ReentrantLock();

  public static void main(String[] args) throws InterruptedException {
    System.out.println("Not synchronized interaction:");
    notSyncInteraction();
    System.out.println("Synchronized interaction with synchronized:");
    SyncInteractionWithSynchronized();
    System.out.println("Synchronized interaction with ReentrantLock:");
    SyncInteractionWithReentrantLock();

  }

  public static synchronized void transfer(
    Bank bank, int from, int to, long x) {
    long balFrom = bank.getAccountBalance(from) - x;
    bank.setAccountBalance(from, balFrom);
    long balTo = bank.getAccountBalance(to) + x;
    bank.setAccountBalance(to, balTo);

  }

  public static void notSyncInteraction() throws InterruptedException {
    //Instantiate:
    Bank bank = new Bank(200, 0L, 1_000L);
    BigInteger initTotal = bank.getSumOfAllAccounts();
    ArrayList<Thread> threads = new ArrayList<>();
//todo: Launch 1000 virtual threads
//(via virtual thread executor or using Thread.ofVirtual() ).
//Each thread repeats the task of transferring funds
//from one random account to another:
    for (int i = 0; i < 1000; ++i) {
      Thread t = Thread.ofVirtual()
              .start(() -> {
                int from = bank.pickRandomAccountId();
                int to = bank.pickRandomAccountId();
                long fromBalance = bank.getAccountBalance(from);
                // WARNING: due to race condition fromBalance could be
                // negative, which leads to exception
                long x = random.nextLong(fromBalance);
                // random x, less than
                // “from”
                // account balance
                // withdraw
                long balFrom = bank.getAccountBalance(from) - x;
                bank.setAccountBalance(from, balFrom);
// deposit
                long balTo = bank.getAccountBalance(to) + x;
                bank.setAccountBalance(to, balTo);
              });
      threads.add(t);
    }
//todo: Wait for all threads to finish.
    for (var thread : threads) {
      thread.join();
    }
//Print final total:
    BigInteger afterTotal = bank.getSumOfAllAccounts();
    String res = String.format(
            """
            Initial total: %s
              Final total: %s
            """, initTotal, afterTotal
    );
    System.out.println(res);
//todo: Compare initial vs. final totals.
  }

  public static void SyncInteractionWithSynchronized() throws InterruptedException {
    //Instantiate:
    Bank bank = new Bank(200, 0L, 1_000L);
    BigInteger initTotal = bank.getSumOfAllAccounts();
    ArrayList<Thread> threads = new ArrayList<>();
//todo: Launch 1000 virtual threads
//(via virtual thread executor or using Thread.ofVirtual() ).
//Each thread repeats the task of transferring funds
//from one random account to another:
    for (int i = 0; i < 1000; ++i) {
      Thread t = Thread.ofVirtual()
              .start(() -> {
                int from = bank.pickRandomAccountId();
                int to = bank.pickRandomAccountId();
                long fromBalance = bank.getAccountBalance(from);
                // WARNING: due to race condition fromBalance could be
                // negative, which leads to exception
                long x = random.nextLong(fromBalance);
                // random x, less than
                // “from”
                // account balance
                // withdraw
                transfer(bank, from, to, x);
              });
      threads.add(t);
    }
//todo: Wait for all threads to finish.
    for (var thread : threads) {
      thread.join();
    }
//Print final total:
    BigInteger afterTotal = bank.getSumOfAllAccounts();
    String res = String.format(
            """
            Initial total: %s
            Final total: %s
            """, initTotal, afterTotal
    );
    System.out.println(res);
  }

  public static void SyncInteractionWithReentrantLock() throws InterruptedException {
    //Instantiate:
    Bank bank = new Bank(200, 0L, 1_000L);
    BigInteger initTotal = bank.getSumOfAllAccounts();
    ArrayList<Thread> threads = new ArrayList<>();
//todo: Launch 1000 virtual threads
//(via virtual thread executor or using Thread.ofVirtual() ).
//Each thread repeats the task of transferring funds
//from one random account to another:
    for (int i = 0; i < 1000; ++i) {
      Thread t = Thread.ofVirtual()
              .start(() -> {
                int from = bank.pickRandomAccountId();
                int to = bank.pickRandomAccountId();
                long fromBalance = bank.getAccountBalance(from);
                // WARNING: due to race condition fromBalance could be
                // negative, which leads to exception
                long x = random.nextLong(fromBalance);
                // random x, less than
                // “from”
                // account balance
                // withdraw
                lock.lock();
                try {
                  long balFrom = bank.getAccountBalance(from) - x;
                  bank.setAccountBalance(from, balFrom);
                  long balTo = bank.getAccountBalance(to) + x;
                  bank.setAccountBalance(to, balTo);
                } finally {

                  lock.unlock();
                }
              });
      threads.add(t);
    }
//todo: Wait for all threads to finish.
    for (var thread : threads) {
      thread.join();
    }
//Print final total:
    BigInteger afterTotal = bank.getSumOfAllAccounts();
    String res = String.format(
            """
            Initial total: %s
            Final total: %s
            """, initTotal, afterTotal
    );
    System.out.println(res);
  }
}
