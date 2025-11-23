package org.GehtSoftHWByAziz.HW7;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class PrimitiveParamExample {

  public void exampleMethod(int count, String name) {
    System.out.printf("count = %d, name = %s%n", count, name);
  }

  public static void main(String[] args) throws Exception {
    Method method = PrimitiveParamExample.class.getMethod("exampleMethod", int.class, String.class);

    System.out.println("Parameters of method:");
    for (Parameter p : method.getParameters()) {
      System.out.println("Name: " + p.getName() + ", Type: " + p.getType().getSimpleName());
    }

    // Имитация вызова через reflection с параметрами
    PrimitiveParamExample instance = new PrimitiveParamExample();

    // Если передать null вместо int, будет ошибка,
    // поэтому для примитива обязательно передавать значение
    method.invoke(instance, 5, "Alice");

    // Если бы Spring не получил параметр "count" из запроса,
    // он бы передал значение по умолчанию 0 для int, не null
    method.invoke(instance, 0, null);
  }
}

