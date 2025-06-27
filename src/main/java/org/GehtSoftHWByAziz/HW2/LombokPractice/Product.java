package org.GehtSoftHWByAziz.HW2.LombokPractice;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Product {
  private String name;
  private int cost;
}

class testProduct {

  public static void main(String[] args) {
    var p = new Product();
    System.out.println("Setting fields...");
    p.setCost(1);
    p.setName("iphone");
    System.out.printf(
            """
            Getting fields
            %s
            %s
            """, p.getCost(), p.getName()
    );
  }
}