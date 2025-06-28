package org.GehtSoftHWByAziz.HW2.Reflection.ReflectionTests;

import junit.framework.AssertionFailedError;
import org.GehtSoftHWByAziz.HW2.Reflection.Test;

public class BunchOfTests {

  @Test
  void failedTest1() {
    assert (2!=2);
  }

  @Test
  void failedTest2() {
    assert (1==2);

  }
  @Test
  void passedTest1() {
    assert (1==2);
  }
}
