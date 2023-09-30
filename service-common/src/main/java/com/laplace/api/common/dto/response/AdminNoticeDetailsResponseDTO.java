package com.laplace.api.common.dto.response;

import com.laplace.api.common.constants.enums.NoticeStatus;
import com.laplace.api.common.model.db.AdminNotice;
import com.laplace.api.common.util.DateUtil;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminNoticeDetailsResponseDTO {

  private String id;
  private String title;
  private String message;
  private String link;
  private String textOfLink;
  private String image;
  private NoticeStatus noticeStatus;
  private Long dateCreated;
  private Long sendTime;
  private Boolean sendEmail;

  public static AdminNoticeDetailsResponseDTO from(AdminNotice adminNotice) {
    return AdminNoticeDetailsResponseDTO.builder()
        .id(adminNotice.getId())
        .title(adminNotice.getTitle())
        .message(adminNotice.getMessage())
        .link(adminNotice.getLink())
        .textOfLink(adminNotice.getTextOfLink())
        .image(adminNotice.getImage())
        .noticeStatus(adminNotice.getNoticeStatus())
        .dateCreated(DateUtil.toEpochMilli(adminNotice.getCreatedOn()))
        .sendTime(DateUtil.toEpochMilli(adminNotice.getSendTime()))
        .sendEmail(adminNotice.getSendEmail())
        .build();
  }
}
