package org.GehtSoftHWByAziz.HW7;

import org.GehtSoftHWByAziz.HW6.Task2.CustomWebServer;

public class RESTCustomWebServer extends CustomWebServer {
  public RESTCustomWebServer(
          int port, int threadPoolSize, boolean useVirtualThreads) {
    super(port, threadPoolSize, useVirtualThreads);
  }
  public void registerController (Object controller) {

  }
}
