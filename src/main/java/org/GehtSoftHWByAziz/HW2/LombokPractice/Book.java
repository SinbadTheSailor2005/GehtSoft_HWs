package org.GehtSoftHWByAziz.HW2.LombokPractice;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Book {
  public String title;
  public String author;
  public int pages;
}


class testBook {
  public static void main (String[] args) {
    var book = new Book("First Law", "Abercrombie", 600);
    System.out.printf("""
                      Generate book with author: %s, title %s, no of Pages: %s
                      """, book.author, book.title, book.pages);
  }
}
