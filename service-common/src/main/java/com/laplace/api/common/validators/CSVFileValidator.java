package com.laplace.api.common.validators;

import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class CSVFileValidator implements ConstraintValidator<ValidCSV, MultipartFile> {

  private ValidCSV validCSV;

  @Override
  public void initialize(ValidCSV constraint) {
    this.validCSV = constraint;
  }

  public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {

    boolean result = true;
    if (file.isEmpty() || !hasValidFileExtension(
        Objects.requireNonNull(file.getOriginalFilename()))) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(validCSV.message())
          .addConstraintViolation();
      result = false;
    }
    return result;
  }

  private boolean hasValidFileExtension(String fileName) {
    log.debug("File Name:" + fileName);
    return fileName.endsWith(".csv");
  }

  private boolean isSupportedContentType(String contentType) {
    log.debug("File Content Type:" + contentType);
    return contentType.equals("text/csv") || contentType.equals("application/vnd.ms-excel");
  }
}
