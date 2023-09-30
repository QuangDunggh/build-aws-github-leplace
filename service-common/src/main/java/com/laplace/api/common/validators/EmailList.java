package com.laplace.api.common.validators;

import com.laplace.api.common.constants.Messages;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = {EmailListValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface EmailList {

  String message() default Messages.INVALID_EMAIL_ADDRESS;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
