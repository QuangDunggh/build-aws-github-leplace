package com.laplace.api.common.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

  private ValidEnum validEnum;

  @Override
  public void initialize(ValidEnum constraintAnnotation) {
    this.validEnum = constraintAnnotation;
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    boolean result = false;
    Object[] enumValues = this.validEnum.enumClass().getEnumConstants();
    if (enumValues != null) {
      for (Object enumValue : enumValues) {
        if (value.equals(enumValue.toString())
            || (this.validEnum.ignoreCase() && value.equalsIgnoreCase(enumValue.toString()))) {
          result = true;
          break;
        }
      }
    }
    return result;
  }
}
