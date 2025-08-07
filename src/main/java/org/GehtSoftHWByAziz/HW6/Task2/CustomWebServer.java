package org.GehtSoftHWByAziz.HW6.Task2;

import org.GehtSoftHWByAziz.HW6.Task1.CustomExecutorService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class CustomWebServer {
  private final int port;
  private final CustomExecutorService executor;
  private ServerSocket serverSocket;
  private volatile boolean running = false;
  private final long startTime = System.currentTimeMillis();
  private final long totalRequests = 0;

  public CustomWebServer(
          int port, int threadPoolSize,
          boolean useVirtualThreads) {
    this.port = port;
    if (useVirtualThreads)
      this.executor =
              CustomExecutorService.newCustomVirtualThreadPool();
    else this.executor =
            CustomExecutorService.newCustomPlatformThreadPool(threadPoolSize);
  }

  public void start() throws IOException {
    // TODO: Implement server startup logic
    serverSocket = new ServerSocket(port);
    while (true) {
      Socket clientSocket = serverSocket.accept();
      Thread.ofVirtual()
              .start(() -> {
                try {
                  handleClient(clientSocket);
                } catch (IOException e) {
                  throw new RuntimeException(e);
                }
              });
      handleClient(clientSocket);
    }
  }


  public void stop() throws IOException {
    serverSocket.close();
    running = false;
  }

  private String getRequestLine(BufferedReader in) throws IOException {
    // Warning: the request should not start with empty lines
    String l;
    l = in.readLine()
            .trim();
    return l;
  }

  private Map<String, String> getHeaders(BufferedReader in) throws IOException {
    String l = "";
    Map<String, String> headers = new HashMap<>();
    System.out.println("Start parsing headers");
    while ((l = in.readLine()) != null && !l.isEmpty()) {
      l = l.trim();
      String[] parts = l.split(": ", 2);
      String key = parts[0];
      String value = parts[1];
      headers.put(key, value);
      System.out.println("Header: " + key + " = " + value);
    }
    return headers;
  }

  private String getBody(BufferedReader in) throws IOException {
    StringBuilder body = new StringBuilder();
    String line;
    while ((line = in.readLine()) != null) {
      body.append(line)
              .append("\n");
    }
    return body.toString()
            .trim();
  }

  private void handleClient(Socket clientSocket) throws IOException {
    // TODO: Handle individual client requests
    BufferedReader in =
            new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
    String requestLine = getRequestLine(in);
    System.out.println("Request line: " + requestLine);
    String[] requestLineParts = requestLine.split(" ");
    String method = requestLineParts[0];
    String requestedResource = requestLineParts[1];
    String version = requestLineParts[2];
    Map<String, String> headers = getHeaders(in);
    String body = getBody(in);
    processRequest(method, requestedResource, out, version, headers, body);
    clientSocket.close();
    in.close();
    out.close();
  }

  private void processRequest(
          String method, String requestedResource,
          PrintWriter out, String version, Map<String, String> headers,
          String body) throws IOException {
    if (method.equals("GET")) {
      if (requestedResource.equals("/")) {

        try {


          String responseBode = Files.readString(Path.of("./static/index" +
                  ".html"));
          out.println(version + " 200 OK");
          out.println();
          out.println(responseBode);
        } catch (IOException e) {
          out.println(version + " 404 Not Found");
          out.println();
          out.println("Resource not found");
        }
      }
      if (requestedResource.startsWith("/static/")) {
        Path path = Path.of("." + requestedResource);
        try {
          String responseBody = Files.readString(path);
          out.println(version + " 200 OK");
          out.println();
          out.println(responseBody);
        } catch (IOException e) {
          out.println(version + " 404 Not Found");
          out.println();
          out.println("Resource not found");
        }
      }
      if (requestedResource.equals("/api/time")) {
        try {
          out.println(version + "200 OK");
          out.println();
          out.println("Current time: " + System.currentTimeMillis());
        } catch (Exception e) {
          out.println(version + " 500 Internal Server Error");
          out.println();
          out.println("Internal server error");
        }
      }
      if (requestedResource.equals("api/stats")) {
        try {
          out.println(version + " 200 OK");
          out.println();
          out.println("Server started at: " + startTime);
          out.println("Total requests: " + totalRequests);
        } catch (Exception e) {
            out.println(version + " 500 Internal Server Error");
            out.println();
            out.println("Internal server error");
        }
      } else {
        out.println(version + " 404 Not Found");
        out.println();
        out.println("Resource not found");
      }
    }
    if (method.equals("POST")) {
      if (requestedResource.equals("/api/echo")) {
        try {
          out.println("" + version + " 200 OK");
          out.println();
          out.println("Echo: " + body);
        }catch (Exception e) {
            out.println(version + " 500 Internal Server Error");
            out.println();
            out.println("Internal server error");
        }
      } else {
        out.println(version + " 404 Not Found");
        out.println();
        out.println("Resource not found");
      }
    }else
      out.println(version + " 405 Method Not Allowed");
      out.println();
      out.println("Method not allowed");

  }
}

