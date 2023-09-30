package com.laplace.api.common.validators;

import com.laplace.api.common.constants.ApplicationConstants;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

  private ValidPassword validPassword;

  public static boolean isValidPasswordLength(String password) {
    return password.length() >= ApplicationConstants.PasswordLength.MIN_LENGTH;
  }

  @Override
  public void initialize(ValidPassword validPassword) {
    this.validPassword = validPassword;
  }

  @Override
  public boolean isValid(String password, ConstraintValidatorContext context) {
    if (StringUtils.isEmpty(password)) {
      return false;
    }

    if (!isValidPasswordLength(password)) {
      return false;
    }

    if (isPasswordValid(password)) {
      return true;
    }
    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate(
        validPassword.message()).addConstraintViolation();
    return false;
  }

  private boolean isPasswordValid(String password) {
    Pattern pattern = Pattern.compile(ApplicationConstants.VALID_PASSWORD_REGEX);
    Matcher matcher = pattern.matcher(password);
    return matcher.find();
  }
}
