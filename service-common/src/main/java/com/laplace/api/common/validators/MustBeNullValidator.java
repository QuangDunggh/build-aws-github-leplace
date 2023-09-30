package com.laplace.api.common.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MustBeNullValidator implements
    ConstraintValidator<MustBeNull, Object> {

  private MustBeNull validObject;

  @Override
  public void initialize(MustBeNull validObject) {
    this.validObject = validObject;
  }

  @Override
  public boolean isValid(Object object, ConstraintValidatorContext context) {
    if (object == null) {
      return true;
    }
    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate(
        validObject.message()).addConstraintViolation();
    return false;
  }
}
