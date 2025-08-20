package org.GehtSoftHWByAziz.HW7;

import org.GehtSoftHWByAziz.HW6.Task2.CustomWebServer;
import org.GehtSoftHWByAziz.HW7.RESTAnnotations.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RESTCustomWebServer extends CustomWebServer {
  private Map<String, Method> httpControllersMethods = new HashMap<>();

  public RESTCustomWebServer(
          int port, int threadPoolSize, boolean useVirtualThreads) {
    super(port, threadPoolSize, useVirtualThreads);
  }

  public void registerController(Object controller) {
    var classRequestMappingAnnotation = controller.getClass()
            .getAnnotation(
                    CustomRequestMapping.class);
    String classRequestMapping = classRequestMappingAnnotation == null ? "" :
            classRequestMappingAnnotation.value();
    for (var method : controller.getClass()
            .getDeclaredMethods()) {
      if (method.isAnnotationPresent(CustomGetMapping.class)) {
        httpControllersMethods.put(
                classRequestMapping + "GET" + method.getAnnotation(
                                CustomGetMapping.class)
                        .value(), method
        );
      }
      if (method.isAnnotationPresent(CustomDeleteMapping.class)) {
        httpControllersMethods.put(
                classRequestMapping + "DELETE" + method.getAnnotation(
                                CustomDeleteMapping.class)
                        .value(), method
        );
      }
      if (method.isAnnotationPresent(CustomPostMapping.class)) {
        httpControllersMethods.put(
                classRequestMapping + "POST" + method.getAnnotation(
                                CustomPostMapping.class)
                        .value(), method
        );
      }
      if (method.isAnnotationPresent(CustomPutMapping.class)) {
        httpControllersMethods.put(
                classRequestMapping + "PUT" + method.getAnnotation(
                                CustomPutMapping.class)
                        .value(), method
        );
      }
      if (method.isAnnotationPresent(CustomPatchMapping.class)) {
        httpControllersMethods.put(
                classRequestMapping + "PATCH" + method.getAnnotation(
                                CustomPatchMapping.class)
                        .value(), method
        );
      }
      if (method.isAnnotationPresent(CustomRequestMapping.class)) {
        var methodRequestMapping =
                method.getAnnotation(CustomRequestMapping.class);
        httpControllersMethods.put(classRequestMapping + methodRequestMapping.method().name() + methodRequestMapping.value(), method);
      }
    }
  }

}
