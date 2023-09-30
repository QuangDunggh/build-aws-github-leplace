package com.laplace.api.common.validators;

import com.amazonaws.util.StringUtils;
import com.laplace.api.common.constants.ApplicationConstants;
import java.util.List;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailListValidator implements ConstraintValidator<EmailList, List<String>> {

  private final Pattern emailPattern = Pattern
      .compile(ApplicationConstants.VALID_EMAIL_ADDRESS_REGEX);

  @Override
  public void initialize(EmailList emailList) {
  }

  @Override
  public boolean isValid(List<String> value, ConstraintValidatorContext context) {
    return !(value == null || value.isEmpty()) &&
        value.stream().filter(e -> !StringUtils.isNullOrEmpty(e))
            .filter(e -> emailPattern.matcher(e).matches()).count() == value.size();
  }
}
