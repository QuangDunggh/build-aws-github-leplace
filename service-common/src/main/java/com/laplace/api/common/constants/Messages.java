package com.laplace.api.common.constants;

import lombok.Data;

@Data
public class Messages {

  public static final String BAD_REQUEST = "Bad Request";
  public static final String SERVER_ERROR = "Internal Server Error";
  public static final String MALFORMED_JSON = "Malformed JSON";
  public static final String VALIDATION_ERROR = "Validation error";
  public static final String MAIL_SENDING_ERROR = "Mail not sent";
  public static final String INVALID_EMAIL_ADDRESS = "Invalid email address";
  public static final String INVALID_ENUM_ARGS = "Invalid %s argument for %s";
  public static final String INVALID_REQUEST_DATE = "Invalid request date";
}
