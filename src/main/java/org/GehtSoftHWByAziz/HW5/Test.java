package org.GehtSoftHWByAziz.HW5;

public class Test {




  public static void main(String [] args) {
    // ВАЖНО: создаем ДВА РАЗНЫХ объекта RequestProcessor
    RequestProcessor proc1 = new RequestProcessor();
    RequestProcessor proc2 = new RequestProcessor();

    new Thread(() -> {
      for (int i = 0; i < 5; i++) proc1.process();
    }, "Поток-А").start();

    new Thread(() -> {
      for (int i = 0; i < 5; i++) proc2.process();
    }, "Поток-Б").start();
  }
}
class RequestProcessor {
  // Общий счетчик для ВСЕХ экземпляров класса
  private static int totalRequests = 0;

  public void process() {
    // Программист думает: "Защищу-ка я инкремент"
    synchronized (this) { // <-- КРИТИЧЕСКАЯ ОШИБКА!
      totalRequests++;
      System.out.println("Обработано заявок: " + totalRequests + " потоком " + Thread.currentThread().getName());
    }
  }
}