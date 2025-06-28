package org.GehtSoftHWByAziz.HW2.Reflection.ReflectionTests;

import org.GehtSoftHWByAziz.HW2.Reflection.Test;

public class AnotherBunchOfTests {
  @Test
  void passedTest1() {
    assert (2!=2);
  }

  @Test
  void failedTest1() {
    assert (1==2);

  }
  @Test
  void passedTest2() {
    assert (1==1);
  }

}
