package org.GehtSoftHWByAziz.HW2.Reflection.ReflectionTests;

import junit.framework.AssertionFailedError;
import org.GehtSoftHWByAziz.HW2.Reflection.Description;
import org.GehtSoftHWByAziz.HW2.Reflection.Test;
import org.GehtSoftHWByAziz.HW2.Reflection.Timeout;

public class BunchOfTests {
  @Description("THIS is description for failed test")
  @Test
  void failedTest1() {
    assert (2!=2);
  }


  @Description("Has timeout")
  @Timeout(10000)
  @Test
  void failedTest2() {
    assert (1==2);

  }
  @Test
  void passedTest1() {
    assert (1==1);
  }

  @Description("Failed due to Timeout")
  @Timeout(0)
  @Test
  void failedTimeoutTest() throws InterruptedException {
    Thread.sleep(5000);
  }
}
