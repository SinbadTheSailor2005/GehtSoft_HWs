package org.GehtSoftHWByAziz.HW6;

public class TestTask implements Runnable{
  @Override
  public void run() {
    try {
      Thread.sleep(10);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
