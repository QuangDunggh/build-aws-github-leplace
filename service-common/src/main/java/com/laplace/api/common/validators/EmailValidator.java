package com.laplace.api.common.validators;

import com.laplace.api.common.constants.ApplicationConstants;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Email;
import org.springframework.util.StringUtils;

public class EmailValidator implements ConstraintValidator<Email, String> {

  private Email email;

  private Pattern emailValidationPattern = Pattern
      .compile(ApplicationConstants.VALID_EMAIL_ADDRESS_REGEX);

  @Override
  public void initialize(Email email) {
    this.email = email;
    if (!StringUtils.isEmpty(this.email.regexp())) {
      emailValidationPattern = Pattern.compile(this.email.regexp());
    }
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (!StringUtils.isEmpty(value) && emailValidationPattern.matcher(value.trim().toLowerCase())
        .matches()) {
      return true;
    }
    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate(this.email.message())
        .addConstraintViolation();

    return false;
  }
}
