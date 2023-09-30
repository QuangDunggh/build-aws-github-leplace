package com.laplace.api.common.validators;

import com.laplace.api.common.util.DateUtil;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.util.ObjectUtils;

public class GreaterThanCurrentDateValidator implements
    ConstraintValidator<GreaterThanCurrentDate, Long> {

  @Override
  public boolean isValid(Long value, ConstraintValidatorContext context) {
    return !(ObjectUtils.isEmpty(value) || value < DateUtil.timeNowToEpochMilli());
  }
}
