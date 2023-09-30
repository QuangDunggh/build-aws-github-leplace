package com.laplace.api.common.util;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.enums.ResponseType;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BaseResponse implements Serializable {

  private static final long serialVersionUID = 1546524853123L;

  @JsonProperty("type")
  private ResponseType responseType;

  @JsonProperty("message")
  private Collection<String> message;

  @JsonProperty("result")
  private Object result;

  @JsonProperty("error")
  private Object error;

  @JsonProperty("code")
  private String code;

  public static BaseResponse create() {
    return create(null, ApplicationConstants.OK_MSG);
  }

  public static BaseResponse create(Object result) {
    return create(result, ApplicationConstants.OK_MSG);
  }

  public static BaseResponse create(String message) {
    return create(null, message);
  }

  public static BaseResponse create(Object result, String message) {
    return create(result, ApplicationConstants.SUCCESS_CODE, message);
  }

  public static BaseResponse create(Object result, String code, String message) {
    return create(ResponseType.RESULT, result, ApplicationConstants.SUCCESS_CODE, message);
  }

  public static BaseResponse create(ResponseType responseType, Object result, String code,
      String message) {
    return BaseResponse.builder()
        .responseType(responseType)
        .result(result)
        .code(code)
        .message(Collections.singletonList(message))
        .build();
  }
}
