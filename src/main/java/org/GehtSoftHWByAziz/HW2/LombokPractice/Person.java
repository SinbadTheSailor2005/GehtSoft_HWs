package org.GehtSoftHWByAziz.HW2.LombokPractice;


import lombok.Data;

/*
Lombok generates:
- setters
- getters
- toString()
- equals()
- hashCode()
- constructor for NonNull and final fields
 */
@Data
public class Person {
  private String name;
  private int age;
  private String email;
}

class testPerson {
  public static void main(String[]args) {
    var p = new Person();
    System.out.println("Lets set fileds");
    p.setAge(20);
    p.setName("Aziz");
    p.setEmail("aziz_email");
    System.out.printf(
            """
            Lets get fields: %s --  %s -- %s
            """, p.getAge(), p.getName(), p.getEmail());
    System.out.printf("""
                       toString(): %s
                       hashCode(): %s
                       """, p.toString(), p.hashCode());

    var pTemp = new Person();

    pTemp.setAge(20);
    pTemp.setName("Aziz");
    pTemp.setEmail("aziz_email");

    System.out.printf("""
                      checking equals(): %s
                      
                      """, pTemp.equals(p));
  }

}