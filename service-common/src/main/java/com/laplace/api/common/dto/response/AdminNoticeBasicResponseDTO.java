package com.laplace.api.common.dto.response;

import com.laplace.api.common.constants.enums.NoticeStatus;
import com.laplace.api.common.model.db.AdminNotice;
import com.laplace.api.common.util.DateUtil;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminNoticeBasicResponseDTO {

  private String id;
  private String title;
  private NoticeStatus noticeStatus;
  private Long dateCreated;
  private Long sentTime;

  public static AdminNoticeBasicResponseDTO from(AdminNotice adminNotice) {
    return AdminNoticeBasicResponseDTO.builder()
        .id(adminNotice.getId())
        .title(adminNotice.getTitle())
        .noticeStatus(adminNotice.getNoticeStatus())
        .dateCreated(DateUtil.toEpochMilli(adminNotice.getCreatedOn()))
        .sentTime(DateUtil.toEpochMilli(adminNotice.getSendTime()))
        .build();
  }
}
