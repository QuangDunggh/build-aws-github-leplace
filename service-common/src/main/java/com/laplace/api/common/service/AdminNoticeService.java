package com.laplace.api.common.service;

import com.laplace.api.common.dto.request.AdminNoticeRequestDto;
import com.laplace.api.common.model.db.AdminNotice;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminNoticeService {

  Optional<AdminNotice> find(String noticeId);

  List<AdminNotice> findAll(ZonedDateTime startTime, ZonedDateTime endTime);

  void insert(AdminNoticeRequestDto adminNoticeRequestDto);

  void update(AdminNoticeRequestDto adminNoticeRequestDto, String noticeId);
}
