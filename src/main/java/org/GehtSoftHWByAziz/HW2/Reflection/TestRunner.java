package org.GehtSoftHWByAziz.HW2.Reflection;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/*
  WARNING: add -ea flag in VM motions!!!
  Otherwise, assert will not throw exception on failure
 */
public class TestRunner {

  private static List<Class<?>> getClasses(
          String packageName) throws ClassNotFoundException {
    String path = packageName.replace('.', '/');
    ClassLoader classLoader = Thread.currentThread()
            .getContextClassLoader();
    var resource = classLoader.getResource(path);
    if (resource == null) return List.of();
    File dir = new File(resource.getFile());

    var res = new ArrayList<Class<?>>();

    if (dir.exists()) {
      try {


        for (var file : dir.list()) {
          if (file.endsWith(".class")) {
            String className = packageName + '.' + file.substring(
                    0,
                    file.length() - 6
            );
            res.add(Class.forName(className));
          }
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }


    return res;
  }

  private static void runTests(
          String packageName) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
    List<Class<?>> classes = getClasses(packageName);

    System.out.printf("""
                           <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< TESTING PACKAGE %s >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                           """, packageName);
    System.out.println("Running tests in package classes...");

    int passedTests = 0;
    int failedTests = 0;
    long totalExecutionTime = 0;
    int scannedClasses = 0;
    for (var classInstance : classes) {
      try {


        Object obj = classInstance.getDeclaredConstructor()
                .newInstance();

        scannedClasses ++;


        var beforeEachMethods = new ArrayList<Method>();
        var afterEachMethods = new ArrayList<Method>();
        var testMethods = new ArrayList<Method>();

        for (var method : classInstance.getDeclaredMethods()) {
          method.setAccessible(true);
          if (method.isAnnotationPresent(BeforeEach.class))
            beforeEachMethods.add(method);
          if (method.isAnnotationPresent(AfterEach.class))
            afterEachMethods.add(method);
          if (method.isAnnotationPresent(Test.class)) testMethods.add(method);
        }
        for (var testMethod : testMethods) {
          for (var beforeEachMethod : beforeEachMethods) {
            beforeEachMethod.invoke(obj);
          }

          long end = 0;
          long begin = 0;
          long duration = 0;
        try {
          begin = System.nanoTime();
          testMethod.invoke(obj);
          end = System.nanoTime();
          duration = end - begin;
          totalExecutionTime += duration;

          System.out.printf("""
                            [PASSED] Test: %s from class: %s 
                            Duration: %s ms
                            """, testMethod.getName(),
                  classInstance.getSimpleName(), duration / 1_000_000.0);
          passedTests ++; // no exception - passed tests increased
        }catch (Exception e) {
          failedTests ++;
          System.out.printf("""
                            [FAILED] Test: %s from class: %s 
                            """, testMethod.getName(),
                  classInstance.getSimpleName());
        }
          for (var afterEachMethod : afterEachMethods) {
            afterEachMethod.invoke(obj);
          }
        }

      } catch (Exception e) {
        // Filtering non-class ofbjects (i.e. annotations, etc.)
      }

    }
    System.out.println("<<<<<<<<<<<<<<<<<<< STATISTICS>>>>>>>>>>>>>>>>>>>");
    double successRate =
            (double) passedTests/((double) (passedTests) + (double)failedTests) * 100;
    System.out.printf("""
                      Total tests: %s
                      Passed: %s
                      Failed: %s
                      Success rate: %s %%
                      Total execution time: %s ms
                      """, failedTests+ passedTests, passedTests, failedTests
            , successRate,
            totalExecutionTime/ 1_000_000.0);
  }

  public static void main(
          String[] args) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
    runTests("org.GehtSoftHWByAziz.HW2.Reflection.ReflectionTests");
  }

}
