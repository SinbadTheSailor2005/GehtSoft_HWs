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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class CustomWebServer {
  private final int port;
  private final CustomExecutorService executor;
  private ServerSocket serverSocket;
  private volatile boolean running = false;
  private final long startTime = System.currentTimeMillis();
  private final AtomicLong totalRequests = new AtomicLong(0);

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
    serverSocket = new ServerSocket(port);
    running = true;
    System.out.println("Creating server on localhost on port " + port);
    while (running) {
      try {
        Socket clientSocket = serverSocket.accept();
        totalRequests.incrementAndGet();
        executor.submit(() -> {
          try (Socket socket = clientSocket) {
            System.out.println("Start handling client request....");
            handleClient(socket);
          } catch (IOException e) {
            if (running) {
              e.printStackTrace();
            }
          }
        });
      } catch (IOException e) {
        if (!running) {
          System.out.println("Server on port " + port + " stopped.");
        } else {
          System.err.println("Error accepting client connection: " + e.getMessage());
        }
      }
    }
  }


  public void stop() {
    running = false;
    try {
      if (serverSocket != null && !serverSocket.isClosed()) {
        serverSocket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    executor.shutdown();
    try {
      if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
        executor.shutdownNow();
      }
    } catch (InterruptedException e) {
      executor.shutdownNow();
    }
  }

  private String getRequestLine(BufferedReader in) throws IOException {
    String l = in.readLine();
    if (l != null) {
      l = l.trim();
    }
    System.out.println("Got requested line: " + l);
    return l;
  }

  private Map<String, String> getHeaders(BufferedReader in) throws IOException {
    String l;
    Map<String, String> headers = new HashMap<>();
    System.out.println("Start parsing headers");
    while ((l = in.readLine()) != null && !l.isEmpty()) {
      l = l.trim();
      String[] parts = l.split(": ", 2);
      if (parts.length == 2) {
        String key = parts[0];
        String value = parts[1];
        headers.put(key, value);
        System.out.println("Header: " + key + " = " + value);
      }
    }
    return headers;
  }

  private String getBody(BufferedReader in, Map<String, String> headers) throws IOException {
    String contentLengthHeader = headers.get("Content-Length");
    if (contentLengthHeader == null) {
      return "";
    }
    int contentLength = Integer.parseInt(contentLengthHeader.trim());
    if (contentLength == 0) {
      return "";
    }
    System.out.println("Start parsing body...");
    char[] bodyChars = new char[contentLength];
    int bytesRead = in.read(bodyChars, 0, contentLength); //TODO: разобраться
    // с этим...
    String body = new String(bodyChars, 0, bytesRead);
    System.out.println("Got body: " + body);
    return body;
  }

  private void handleClient(Socket clientSocket) throws IOException {
    BufferedReader in =
            new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
    String requestLine = getRequestLine(in);
    if (requestLine == null || requestLine.isEmpty()) {
      return;
    }
    System.out.println("Request line: " + requestLine);
    String[] requestLineParts = requestLine.split(" ");
    String method = requestLineParts[0];
    String requestedResource = requestLineParts[1];
    String version = requestLineParts[2];
    Map<String, String> headers = getHeaders(in);
    String body = "";
    if (method.equalsIgnoreCase("PUT") || method.equalsIgnoreCase("POST")) {
      body = getBody(in, headers);
    }
    processRequest(method, requestedResource, out, version, headers, body);
  }

  private void processRequest(
          String method, String requestedResource,
          PrintWriter out, String version, Map<String, String> headers,
          String body) {
    if (method.equals("GET")) {
      if (requestedResource.equals("/")) {
        try {
          String responseBody = Files.readString(Path.of("./static/index.html"));
          out.println(version + " 200 OK");
          out.println("Content-Type: text/html");
          out.println("Content-Length: " + responseBody.getBytes().length);
          out.println();
          out.println(responseBody);
        } catch (IOException e) {
          out.println(version + " 404 Not Found");
          out.println();
          out.println("Resource not found");
        }
      } else if (requestedResource.startsWith("/static/")) {
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
      } else if (requestedResource.equals("/api/time")) {
        String responseBody = "Current time: " + System.currentTimeMillis();
        out.println(version + " 200 OK");
        out.println("Content-Length: " + responseBody.getBytes().length);
        out.println();
        out.println(responseBody);
      } else if (requestedResource.equals("/api/stats")) {
        String responseBody = "Server started at: " + startTime + "\n" +
                "Total requests: " + totalRequests.get();
        out.println(version + " 200 OK");
        out.println("Content-Length: " + responseBody.getBytes().length);
        out.println();
        out.println(responseBody);
      } else {
        out.println(version + " 404 Not Found");
        out.println();
        out.println("Resource not found");
      }
    } else if (method.equals("POST")) {
      if (requestedResource.equals("/api/echo")) {
        String responseBody = "Echo: " + body;
        out.println(version + " 200 OK");
        out.println("Content-Length: " + responseBody.getBytes().length);
        out.println();
        out.println(responseBody);
      } else {
        out.println(version + " 404 Not Found");
        out.println();
        out.println("Resource not found");
      }
    } else {
      out.println(version + " 405 Method Not Allowed");
      out.println();
      out.println("Method not allowed");
    }
  }
}