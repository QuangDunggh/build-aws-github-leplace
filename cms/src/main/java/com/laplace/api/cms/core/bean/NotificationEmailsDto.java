package com.laplace.api.cms.core.bean;

import com.laplace.api.common.constants.ErrorCode;
import com.laplace.api.common.validators.EmailList;
import java.util.List;
import lombok.Data;

@Data
public class NotificationEmailsDto {

  @EmailList(message = ErrorCode.INVALID_EMAIL_PATTERN)
  List<String> emails;
}
