package com.laplace.api.common.service.impl;

import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

import com.laplace.api.common.constants.enums.NoticeStatus;
import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.converter.AdminNoticeConverter;
import com.laplace.api.common.dto.request.AdminNoticeRequestDto;
import com.laplace.api.common.model.db.AdminNotice;
import com.laplace.api.common.repository.db.AdminNoticeRepository;
import com.laplace.api.common.service.AdminNoticeService;
import com.laplace.api.common.service.LaplaceLambdaService;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminNoticeServiceImpl implements AdminNoticeService {

  private final AdminNoticeRepository adminNoticeRepository;
  private final LaplaceLambdaService laplaceLambdaService;

  @Autowired
  public AdminNoticeServiceImpl(
      AdminNoticeRepository adminNoticeRepository,
      LaplaceLambdaService laplaceLambdaService) {
    this.adminNoticeRepository = adminNoticeRepository;
    this.laplaceLambdaService = laplaceLambdaService;
  }


  @Override
  public Optional<AdminNotice> find(String noticeId) {
    return adminNoticeRepository.findById(noticeId);
  }

  @Override
  public List<AdminNotice> findAll(ZonedDateTime startTime, ZonedDateTime endTime) {
    return adminNoticeRepository.findByCreatedOnBetweenOrderByCreatedOnDesc(startTime, endTime);
  }

  @Override
  public void insert(AdminNoticeRequestDto adminNoticeRequestDto) {
    adminNoticeRepository.save(AdminNoticeConverter.make(adminNoticeRequestDto));
    checkAndInvokeAdminNoticeLambda(adminNoticeRequestDto.getNoticeStatus());
  }

  @Override
  public void update(AdminNoticeRequestDto adminNoticeRequestDto, String noticeId) {
    AdminNotice adminNotice = find(noticeId)
        .orElseThrow(() -> throwApplicationException(ResultCodeConstants.NOT_FOUND));
    if (!adminNotice.getNoticeStatus().equals(NoticeStatus.DRAFT)) {
      throw throwApplicationException(ResultCodeConstants.NOTIFICATION_ALREADY_SENT);
    }
    adminNoticeRepository.save(AdminNoticeConverter.update(adminNoticeRequestDto, adminNotice));
    checkAndInvokeAdminNoticeLambda(adminNoticeRequestDto.getNoticeStatus());
  }

  private void checkAndInvokeAdminNoticeLambda(NoticeStatus noticeStatus) {
    if (noticeStatus.equals(NoticeStatus.READY)) {
      laplaceLambdaService.adminNoticeHandler();
    }
  }
}
