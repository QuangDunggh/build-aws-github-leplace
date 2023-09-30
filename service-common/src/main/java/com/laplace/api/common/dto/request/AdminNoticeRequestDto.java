package com.laplace.api.common.dto.request;

import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.laplace.api.common.constants.ErrorCode;
import com.laplace.api.common.constants.enums.NoticeStatus;
import com.laplace.api.common.constants.enums.ResultCodeConstants;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.util.ObjectUtils;

@Data
public class AdminNoticeRequestDto {


  @NotEmpty(message = ErrorCode.INVALID_ARGUMENT)
  private String title;

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private String message;

  private String link;

  private String textOfLink;

  private String image;

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private NoticeStatus noticeStatus;

  private Long sentTime;

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private Boolean sendEmail;

  @JsonIgnore
  public void validate() {
    if ((noticeStatus.equals(NoticeStatus.DRAFT) && ObjectUtils.isEmpty(sentTime))
        || noticeStatus.equals(NoticeStatus.SENT)) {
      throw throwApplicationException(ResultCodeConstants.INVALID_ARGUMENT);
    }
  }
}
