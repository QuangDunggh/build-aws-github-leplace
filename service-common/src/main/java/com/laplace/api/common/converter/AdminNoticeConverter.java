package com.laplace.api.common.converter;

import com.laplace.api.common.dto.request.AdminNoticeRequestDto;
import com.laplace.api.common.model.db.AdminNotice;
import com.laplace.api.common.util.DateUtil;
import java.util.Objects;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class AdminNoticeConverter {

  public static AdminNotice make(AdminNoticeRequestDto requestDto) {
    return AdminNotice.builder()
        .id(DateUtil.getUniqueTimeBasedUUID())
        .title(requestDto.getTitle())
        .message(requestDto.getMessage())
        .textOfLink(requestDto.getTextOfLink())
        .link(requestDto.getLink())
        .image(requestDto.getImage())
        .noticeStatus(requestDto.getNoticeStatus())
        .sendTime(ObjectUtils.isEmpty(requestDto.getSentTime()) ? DateUtil.timeNow()
            : DateUtil.fromEpochMilli(requestDto.getSentTime()))
        .sendEmail(requestDto.getSendEmail())
        .createdOn(DateUtil.timeNow())
        .build();
  }

  public static AdminNotice update(AdminNoticeRequestDto requestDto, AdminNotice adminNotice) {
    return AdminNotice.builder()
        .id(adminNotice.getId())
        .title(checkNull(requestDto.getTitle(), adminNotice.getTitle()))
        .message(checkNull(requestDto.getMessage(), adminNotice.getMessage()))
        .textOfLink(checkNull(requestDto.getTextOfLink(), adminNotice.getTitle()))
        .link(checkNull(requestDto.getLink(), adminNotice.getTitle()))
        .image(checkNull(requestDto.getImage(), adminNotice.getImage()))
        .noticeStatus(checkNull(requestDto.getNoticeStatus(), requestDto.getNoticeStatus()))
        .sendTime(
            checkNull(DateUtil.fromEpochMilli(requestDto.getSentTime()), adminNotice.getSendTime()))
        .sendEmail(checkNull(requestDto.getSendEmail(), adminNotice.getSendEmail()))
        .createdOn(adminNotice.getCreatedOn())
        .updatedOn(DateUtil.timeNow())
        .build();
  }

  private static <T> T checkNull(T current, T old) {
    return Objects.isNull(current) ? old : current;
  }
}
