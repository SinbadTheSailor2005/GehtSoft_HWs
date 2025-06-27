package org.GehtSoftHWByAziz.HW2.LombokPractice;

import lombok.Builder;
/*
  Provides builder pattern
 */
@Builder
public class House {
  public String address;
  public int rooms;
  public int area;
  public int price;

}
class testHouse {
  public static void main (String []args) {
    var house =
            House.builder().address("Universitetskaya 1").area(15).price(5000).rooms(1).build();
    System.out.printf("""
                      Built house:
                      %s
                      %s
                      %s
                      %s
                      """,house.address,house.rooms,house.area, house.price);
  }

}