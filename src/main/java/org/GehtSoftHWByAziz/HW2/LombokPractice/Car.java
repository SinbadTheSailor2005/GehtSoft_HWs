package org.GehtSoftHWByAziz.HW2.LombokPractice;

import lombok.RequiredArgsConstructor;
/*
  Generated constructor for final and notNull fields
 */
@RequiredArgsConstructor
public class Car {

  private final String brand;
  private final String model;
}


class testCar {


  public static void main (String[] args) {
    System.out.println("Generating object by lombok constructor");
    var car = new Car("BMW", "X%"); //
  }
}
