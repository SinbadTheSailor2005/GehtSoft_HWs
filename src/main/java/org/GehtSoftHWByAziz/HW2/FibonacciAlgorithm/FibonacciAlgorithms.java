package org.GehtSoftHWByAziz.HW2.FibonacciAlgorithm;

import java.util.HashMap;

public class FibonacciAlgorithms {
  /**
   * Memoized implementation of Fibonacci sequence
   * Time Complexity: O(N), since each f(n) calculates only ones for
   * Space Complexity: O(N) since in worst case we will put all numbers to
   * hashmap
   *
   * Explanation: By caching intermediate results, we avoid
   * redundant calculations. Each number from 0 to n is
   * calculated exactly once.
   */
    private HashMap<Integer, Long> map = new HashMap<>();

  public static long fibonacciMemoized(int n, HashMap<Integer, Long> map) {
    if (n == 0) return 0;
    if (n == 1) return 1;
    if (map.containsKey(n)) return map.get(n);
    else {
      map.put(n, fibonacciMemoized(n - 1, map) + fibonacciMemoized(n - 2, map));
      return map.get(n);
    }
  }

  /**
   * Iterative implementation of Fibonacci sequence
   * Time Complexity: O(n) - single loop from 0 to n
   * Space Complexity: O(1) - constant space usage
   *
   * Explanation: Uses bottom-up approach with only two variables
   * to track previous values, eliminating recursion overhead.
   */
  public static long fibonacciIterative(int n) {

    long a = 0;
    long b = 1;
    if ( n == 0 )return  a;
    if (n == 1 ) return b;
    for (int i = 0; i < n; i ++) {
      long temp = a;
      a = a + b;
      b = temp;
    }
    return a;

  }
}


class testFib {


  public static void main (String[] args) {
    var map = new HashMap<Integer, Long>();
    int n = 10;
    long res1 = FibonacciAlgorithms.fibonacciMemoized(n, map);
    long res2 = FibonacciAlgorithms.fibonacciIterative(n);
    System.out.println(res1 + " " + res2);
  }
}
