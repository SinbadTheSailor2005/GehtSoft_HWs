package org.GehtSoftHWByAziz.HW2.ArrayOperations;

import org.GehtSoftHWByAziz.HW2.FibonacciAlgorithm.FibonacciAlgorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

public class ArrayOperations {
  /** Shift array elements using System.arraycopy
   * According to SystemCopy is generally  faster than manual copying
   * especially when shift value is low
   *
   * */
  public static void shiftLeftSystemCopy(int[] array, int positions) {
    int normalized_pos  = positions % array.length;
    int[] temp = new int[array.length];
    System.arraycopy(array,normalized_pos,temp, 0,
            array.length - normalized_pos);
    System.arraycopy(array,0, temp, array.length - normalized_pos, normalized_pos);
    System.arraycopy(temp, 0, array, 0, array.length);
  }
  /** Shift array elements using manual for loop
   *
   * Time / Space Complexity = O(N)
   * */
  public static void shiftLeftManualLoop(int[] array, int positions) {
    // todo: Implementation using for loop
    int normalized_pos = positions % array.length;
    int [] temp = new int[array.length];

    for (int i = 0 ; i < array.length - normalized_pos; i ++) {
      temp[i] = array[i + normalized_pos];
    }
  for (int i = 0 ; i < normalized_pos; i ++)  {
    temp[array.length - normalized_pos + i] = array[i];
  }


    for (int i = 0 ; i < array.length; i ++) {
      array[i] = temp[i];
    }
  }

/*
  Time / Space Complexity O(N)
 */
private static void testSystemCopy(int size, int shift, BiConsumer<int[],
        Integer>func) {

    Runtime runtime = Runtime.getRuntime();

    long start = System.nanoTime();

    func.accept(new int[size], shift);
    long stop = System.nanoTime();


    double duration = (stop - start) /1_000_000.0;

    System.out.printf("""
                      Size = %s Shift = %s
                      Total execution time = %s ms
                      """, size,shift, duration);
  }



  public static void main (String[]args) {
    ArrayList<Integer> sizes = new ArrayList<>(List.of(1000,10000,100000,
            1000000));
    ArrayList<Integer>shiftPositions = new ArrayList<>(List.of(1,10,100,1000));
    System.out.println("Benchmark Manual Approach");
    for (int size : sizes) {
      for (int shift : shiftPositions) {
        testSystemCopy(size, shift, ArrayOperations::shiftLeftManualLoop);
      }
    }

    System.out.println("\n\nBenchmark System copy approach");
    for (int size : sizes) {
      for (int shift : shiftPositions) {
        testSystemCopy(size, shift, ArrayOperations::shiftLeftSystemCopy);
      }
    }

//    int[] a = new int[]{1,2,3,4,5};
//
//    int[] b = new int[]{1,2,3,4,5};
//    shiftLeftSystemCopy(a,3);
//    shiftLeftManualLoop(b, 3);
//    System.out.println(Arrays.toString(a) + "\n" + Arrays.toString(b));
  }
}
