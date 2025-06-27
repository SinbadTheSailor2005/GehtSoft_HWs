package org.GehtSoftHWByAziz.HW2.LombokPractice;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
// методы get и set возращают this
@Accessors(chain = true)
@Setter
@Getter
public class Configuration {
  private int A;
  private int B;
}

class testConfiguration {
  public static void main (String [] args) {
    var config = new Configuration();
    config.setA(1).setB(2);
    System.out.printf(
                      """
                      Check setted fields:
                      %s 
                      %s
                      
                      """, config.getA(), config.getB());
  }
}
