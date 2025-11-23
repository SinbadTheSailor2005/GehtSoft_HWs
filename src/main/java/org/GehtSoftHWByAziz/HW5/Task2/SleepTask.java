package org.GehtSoftHWByAziz.HW5.Task2;

public class SleepTask implements Runnable{
  @Override
  public void run() {
    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
