package org.GehtSoftHWByAziz.HW2.Reflection;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


/*
  WARNING: add -ea flag in VM motions/actions!!!
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

    int[] passedTests = new int[]{0};
    int []failedTests = new int[]{0};
    long [] totalExecutionTime =new long[] {0};
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
          Description desc = testMethod.getAnnotation(Description.class);
          Timeout timeout = testMethod.getAnnotation(Timeout.class);
          if (desc != null) {
            System.out.println(desc.value());
          }
          if (timeout != null ) {
            try(
            ExecutorService exec = Executors.newSingleThreadExecutor();
            ) {

              Runnable task = () -> {
                runTest(testMethod,failedTests,passedTests,
                        totalExecutionTime,obj,classInstance);
              };
              Future<?> future = exec.submit(task);
              try {
                future.get(timeout.value(), TimeUnit.MILLISECONDS);
              } catch (TimeoutException e) {
                failedTests[0] ++;
                System.out.printf("""
                            TIMEOUT EXCEPTION
                            
                            """, testMethod.getName(),
                        classInstance.getSimpleName());
              } finally {
              exec.shutdownNow();
              }
            }
          } else {
            runTest(testMethod, failedTests, passedTests, totalExecutionTime,
                    obj, classInstance);
          }
          for (var afterEachMethod : afterEachMethods) {
            afterEachMethod.invoke(obj);
          }
        }

      } catch (Exception e) {
        // Filtering non-class objects (i.e. annotations, etc.)
      }

    }
    System.out.println("<<<<<<<<<<<<<<<<<<< STATISTICS>>>>>>>>>>>>>>>>>>>");
    double successRate =
            (double) passedTests[0]/((double) (passedTests[0]) + (double)failedTests[0]) * 100;
    System.out.printf("""
                      Total tests: %s
                      Passed: %s
                      Failed: %s
                      Success rate: %s %%
                      Total execution time: %s ms
                      """, failedTests[0]+ passedTests[0], passedTests[0],
            failedTests[0]
            , successRate,
            totalExecutionTime[0]/ 1_000_000.0);
  }

  private static void runTest(
          Method testMethod, int[] failedTests,int[] passedTests,
          long[] totalExecutionTime, Object obj, Class<?> classInstance) {

    long end = 0;
    long begin = 0;
    long duration = 0;
    try {
      begin = System.nanoTime();
      testMethod.invoke(obj);
      end = System.nanoTime();
      duration = end - begin;
      totalExecutionTime[0] += duration;

      System.out.printf("""
                            [PASSED] Test: %s from class: %s 
                            Duration: %s ms
                            
                            """, testMethod.getName(),
              classInstance.getSimpleName(), duration / 1_000_000.0);
      passedTests[0] ++; // no exception - passed tests increased
    }catch (Exception e) {
      failedTests[0] ++;
      System.out.printf("""
                            [FAILED] Test: %s from class: %s 
                            
                            """, testMethod.getName(),
              classInstance.getSimpleName());
    }
  }


  public static void main(
          String[] args) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
    runTests("org.GehtSoftHWByAziz.HW2.Reflection.ReflectionTests");
  }

}
