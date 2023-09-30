package com.laplace.api.security.marker;

import com.laplace.api.common.constants.enums.EntityType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker to define access control on method.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.LOCAL_VARIABLE})
public @interface AccessControl {

  /**
   * @return the entity to check for access.
   */
  EntityType[] value();

}
