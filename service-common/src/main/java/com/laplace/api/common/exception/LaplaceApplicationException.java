package com.laplace.api.common.exception;

import com.laplace.api.common.constants.enums.ResultCodeConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class LaplaceApplicationException extends RuntimeException {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 1436995162658277359L;

  private ResultCodeConstants resultCode;

  private String errorCode;

  private String requestId;

  private transient Object[] args;

  @Builder.Default
  private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

  public LaplaceApplicationException(ResultCodeConstants resultCode, String errorCode,
      HttpStatus status, Object... args) {
    super("Result Code=" + resultCode + ", Error Code=" + errorCode);
    this.resultCode = resultCode;
    this.errorCode = errorCode;
    this.status = status;
    this.requestId = null;
    this.args = args;
  }

}
