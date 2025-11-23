package org.GehtSoftHWByAziz.HW2.LombokPractice;

import lombok.SneakyThrows;

import java.io.IOException;

public class SneakyThrowTest {

  @SneakyThrows
  public static void main (String []args)  {
    // не нужно
    // писать throws IOException
    throw new IOException();
  }
}
