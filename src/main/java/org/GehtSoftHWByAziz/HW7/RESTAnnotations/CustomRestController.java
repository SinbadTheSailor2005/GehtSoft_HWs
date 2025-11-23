package org.GehtSoftHWByAziz.HW7.RESTAnnotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
/**
 * RestController = Controller + ResponseBody
 */
public @interface CustomRestController {
}
