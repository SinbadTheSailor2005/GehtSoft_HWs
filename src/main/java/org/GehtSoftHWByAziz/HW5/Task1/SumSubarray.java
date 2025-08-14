package org.GehtSoftHWByAziz.HW5.Task1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class SumSubarray implements Callable<Long> {
  private  short[] subArray;

  public SumSubarray(short[] subArray) {
    this.subArray = subArray;
  }

  @Override
  public Long call() throws Exception {

    return IntStream.range(0, subArray.length).mapToLong(i -> (long) subArray[i]).sum();
  }
}
