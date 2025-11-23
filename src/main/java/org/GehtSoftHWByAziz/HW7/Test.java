package org.GehtSoftHWByAziz.HW7;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class Test {
  private static <T> HttpResponse<T> sendRequest(
          HttpClient client,
          HttpRequest request,
          HttpResponse.BodyHandler<T> handler)
          throws IOException, InterruptedException {
    HttpResponse<T> response = client.send(request, handler);

    return response;
  }

  private static <T> void printResponse(HttpResponse<T> response) {
    System.out.println("Status code: " + response.statusCode());
    System.out.println("Body: " + response.body());
  }

  public static void main(
          String[] args) throws IOException, URISyntaxException {
    // Test with virtual threads
    RESTCustomWebServer virtualServer = new RESTCustomWebServer(8080, 100, true);

    // Test with platform threads
    RESTCustomWebServer platformServer = new RESTCustomWebServer(8081, 50,
            false);

    virtualServer.registerController(new UserController(new UserService()));
    platformServer.registerController(new UserController(new UserService()));
    String virtualServerUrl = "http://localhost:8080";
    String platformServerUrl = "http://localhost:8081";

    List<String> servers = List.of(virtualServerUrl, platformServerUrl);
    try {
      new Thread(() -> {
        try {
          virtualServer.start();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }).start();
      new Thread(() -> {
        try {
          platformServer.start();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }).start();


      System.out.println("Servers started:");
      System.out.println("Virtual thread server: http://localhost:8080");
      System.out.println("Platform thread server: http://localhost:8081");

//      // Keep servers running
//      for (var server : servers) {
//
//        HttpClient client =
//                HttpClient.newBuilder()
//                        .build();
//        HttpRequest getRoot =
//                HttpRequest.newBuilder()
//                        .uri(new URI(server + "/"))
//                        .GET()
//                        .build();
//        HttpRequest getTime = HttpRequest.newBuilder()
//                .uri(new URI(server +
//                        "/api/time"))
//                .GET()
//                .build();
//        HttpRequest getStats =
//                HttpRequest.newBuilder()
//                        .uri(new URI(server + "/api/stats"))
//                        .GET()
//                        .build();
//        HttpRequest postEcho =
//                HttpRequest.newBuilder()
//                        .uri(new URI(server + "/api/echo"))
//                        .POST(HttpRequest.BodyPublishers.ofString(
//                                "AZIZ SENDING MESSAGE"))
//                        .build();
//        List <HttpRequest> methods = List.of(getTime,getStats, getRoot,postEcho);
//        for (var m : methods) {
//          var response = sendRequest(client, m,
//                  HttpResponse.BodyHandlers.ofString());
//          printResponse(response);
//          System.out.println("-".repeat(10));
//        }
//      }

      Thread.sleep(6000000); // Run for 1 minute

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      virtualServer.stop();
      platformServer.stop();
    }
  }

}
