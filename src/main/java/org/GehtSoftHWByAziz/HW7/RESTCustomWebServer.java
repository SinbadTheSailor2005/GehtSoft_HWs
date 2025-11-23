package org.GehtSoftHWByAziz.HW7;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.GehtSoftHWByAziz.HW6.Task2.CustomWebServer;
import org.GehtSoftHWByAziz.HW7.RESTAnnotations.*;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RESTCustomWebServer extends CustomWebServer {
  private final Map<String, Method> httpControllersMethods = new HashMap<>();
  private static final ObjectMapper mapper = new ObjectMapper();
  private Object controller;

  public RESTCustomWebServer(
          int port, int threadPoolSize, boolean useVirtualThreads) {
    super(port, threadPoolSize, useVirtualThreads);
  }

  public void registerController(Object controller) {
    this.controller = controller;
    var classRequestMappingAnnotation = controller.getClass()
            .getAnnotation(
                    CustomRequestMapping.class);
    String classRequestMapping = classRequestMappingAnnotation == null ? "" :
            classRequestMappingAnnotation.value();
    for (var method : controller.getClass()
            .getDeclaredMethods()) {
      if (method.isAnnotationPresent(CustomGetMapping.class)) {
        httpControllersMethods.put(
                "GET " + classRequestMapping + method.getAnnotation(
                                CustomGetMapping.class)
                        .value(), method
        );
      }
      if (method.isAnnotationPresent(CustomDeleteMapping.class)) {
        httpControllersMethods.put(
                "DELETE " + classRequestMapping + method.getAnnotation(
                                CustomDeleteMapping.class)
                        .value(), method
        );
      }
      if (method.isAnnotationPresent(CustomPostMapping.class)) {
        httpControllersMethods.put(
                "POST " + classRequestMapping + method.getAnnotation(
                                CustomPostMapping.class)
                        .value(), method
        );
      }
      if (method.isAnnotationPresent(CustomPutMapping.class)) {
        httpControllersMethods.put(
                "PUT " + classRequestMapping + method.getAnnotation(
                                CustomPutMapping.class)
                        .value(), method
        );
      }

      if (method.isAnnotationPresent(CustomRequestMapping.class)) {
        var methodRequestMapping =
                method.getAnnotation(CustomRequestMapping.class);
        httpControllersMethods.put(
                methodRequestMapping.method()
                        .name() + " " + classRequestMapping + methodRequestMapping.value(),
                method
        );
      }
    }
  }

  private String removeQueryParameters(String resource) {
    if (resource.contains("?")) {
      return resource.split("\\?", 2)[0];
    }
    return resource;
  }

  private Object convertToParameterType(String value, Class<?> targetType) {
    if (value == null) {
      return getDefaultValueForType(targetType);
    }
    if (targetType == String.class) {
      return value;
    } else if (targetType == int.class || targetType == Integer.class) {
      return Integer.parseInt(value);
    } else if (targetType == long.class || targetType == Long.class) {
      return Long.parseLong(value);
    } else if (targetType == boolean.class || targetType == Boolean.class) {
      return Boolean.parseBoolean(value);
    } else if (targetType == double.class || targetType == Double.class) {
      return Double.parseDouble(value);
    }

    return null;
  }

  private Object getDefaultValueForType(Class<?> type) {
    if (type.isPrimitive()) {
      if (type == boolean.class) return false;
      if (type == char.class) return '\0';
      if (type == byte.class) return (byte) 0;
      if (type == short.class) return (short) 0;
      if (type == int.class) return 0;
      if (type == long.class) return 0L;
      if (type == float.class) return 0f;
      if (type == double.class) return 0d;
    }
    return null;
  }
// /a/ {id}
  private Map<String, String> extractPathVariables(
          String routeTemplate, String requestedUri) {
    Map<String, String> variables = new HashMap<>();


    List<String> variableNames = new ArrayList<>();
    Pattern placeholderPattern = Pattern.compile("\\{([^/]+?)\\}");
    Matcher placeholderMatcher = placeholderPattern.matcher(routeTemplate);

    while (placeholderMatcher.find()) {
      variableNames.add(placeholderMatcher.group(1));
    }

    String regex = routeTemplate.replaceAll("\\{[^/]+?\\}", "([^/]+)");
    Pattern pathPattern = Pattern.compile(regex);
    Matcher pathMatcher = pathPattern.matcher(requestedUri);

    if (pathMatcher.find()) {
      for (int i = 0; i < variableNames.size(); i++) {

        String value = pathMatcher.group(i + 1);
        variables.put(variableNames.get(i), value);
      }
    }

    return variables;
  }

  @Override
  protected void processRequest(
          String method, String requestedResource,
          PrintWriter out, String version,
          Map<String, String> headers, String body) {

    String cleanResource = removeQueryParameters(requestedResource);
    Method handler = getHandler(method, cleanResource);

    if (handler == null) {
      out.println(version + " 404 Not Found");
      out.println();
      return;
    }

    String routeKey = httpControllersMethods.entrySet()
            .stream()
            .filter(entry -> entry.getValue()
                    .equals(handler))
            .findFirst()
            .map(Map.Entry::getKey)
            .orElse("");

    String routeTemplate = routeKey.split(" ", 2)[1];

    Map<String, String> pathVariables =
            extractPathVariables(routeTemplate, cleanResource);


    Map<String, String> requestParameters =
            getRequestedResources(requestedResource);
    Parameter[] parameters = handler.getParameters();
    List<Object> args = new ArrayList<>();

    for (Parameter parameter : parameters) {
      if (parameter.isAnnotationPresent(CustomPathVariable.class)) {
        String varName = parameter.getAnnotation(CustomPathVariable.class)
                .value();

        if (pathVariables.containsKey(varName)) {
          String stringValue = pathVariables.get(varName);
          args.add(convertToParameterType(stringValue, parameter.getType()));
        } else {
          System.out.println(
                  "Path variable '" + varName + "' not found in route " + routeTemplate);
          args.add(null);
        }

      } else if (parameter.isAnnotationPresent(CustomRequestParam.class)) {
        String value = parameter.getAnnotation(
                        CustomRequestParam.class)
                .value();
        String stringValue = requestParameters.get(value);
        args.add(convertToParameterType(stringValue, parameter.getType()));
      } else if (parameter.isAnnotationPresent(CustomRequestBody.class)) {
        if (parameter.getType()
                .equals(String.class)) {
          args.add(body);
        } else {
          try {
            Object obj = mapper.readValue(body, parameter.getType());
            args.add(obj);
          } catch (Exception e) {
            System.out.println("Failed to parse request body: " + body +
                    "\n" + e.getMessage());
            out.println(version + " 400 Bad Request");
            out.println();
            return;
          }
        }
      } else {
        args.add(null);
      }
    }
    try {
      Object response = handler.invoke(controller, args.toArray());
      String jsonResponse = mapper.writeValueAsString(response);
      out.println(version + " 200 OK");
      out.println("Content-Type: application/json");
      out.println();
      out.println(jsonResponse);
    } catch (Exception e) {
      System.out.println("failed to invoke handler for " + method + " " +
              requestedResource + "\n" + e.getMessage());
      out.println(version + " 500 Internal Server Error");
      out.println();
    }
  }
// /{id}
  private Method getHandler(String method, String requestedResource) {
    // handing query parameters by removing them from the resource path
    for (var entry : httpControllersMethods.entrySet()) {
      String key = entry.getKey();
      String path = key.split(" ", 2)[1];
      Pattern pathVariablePattern = Pattern.compile("\\{([^/]+)\\}");
      String regexPath = pathVariablePattern.matcher(path)
              .replaceAll("([^/]+)");
      if (key.startsWith(method + " ") &&
              Pattern.matches(
                      regexPath,
                      removeQueryParameters(requestedResource)
              )) {
        return entry.getValue();
      }
    }
    return httpControllersMethods.get(
            method + " " + removeQueryParameters(requestedResource));
  }

  private Map<String, String> getRequestedResources(String requestedResource) {
    Map<String, String> requestParameters = new HashMap<>();
    if (requestedResource.contains("?")) {
      String[] parts = requestedResource.split("\\?", 2);
      requestedResource = parts[0];
      String queryString = parts[1];
      String[] params = queryString.split("&");
      for (String param : params) {
        String[] keyValue = param.split("=", 2);
        if (keyValue.length == 2) {
          requestParameters.put(keyValue[0], keyValue[1]);
        } else if (keyValue.length == 1) {
          requestParameters.put(keyValue[0], "");
        }
      }
    }
    return requestParameters;
  }
}
