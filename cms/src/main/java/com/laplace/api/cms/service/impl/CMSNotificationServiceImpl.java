package com.laplace.api.cms.service.impl;

import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

import com.laplace.api.cms.service.CMSNotificationService;
import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.dto.request.AdminNoticeRequestDto;
import com.laplace.api.common.dto.response.AdminNoticeBasicResponseDTO;
import com.laplace.api.common.dto.response.AdminNoticeDetailsResponseDTO;
import com.laplace.api.common.service.AdminNoticeService;
import com.laplace.api.common.util.DateUtil;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CMSNotificationServiceImpl implements CMSNotificationService {

  private final AdminNoticeService adminNoticeService;

  @Autowired
  public CMSNotificationServiceImpl(AdminNoticeService adminNoticeService) {
    this.adminNoticeService = adminNoticeService;
  }

  @Override
  public List<AdminNoticeBasicResponseDTO> getAdminNotices(Integer month, Integer year,
      Pageable pageable) {
    ZonedDateTime startDate = DateUtil.getFirstDayOfMonth(year, month);
    ZonedDateTime endDate = DateUtil.getLastDayOfMonth(year, month);
    return adminNoticeService.findAll(startDate, endDate).stream()
        .map(AdminNoticeBasicResponseDTO::from)
        .collect(Collectors.toList());
  }

  @Override
  public AdminNoticeDetailsResponseDTO getAdminNoticeDetails(String notificationId) {
    return AdminNoticeDetailsResponseDTO.from(adminNoticeService.find(notificationId)
        .orElseThrow(() -> throwApplicationException(ResultCodeConstants.NOT_FOUND)));
  }

  @Override
  public void saveAdminNotice(AdminNoticeRequestDto adminNoticeRequestDto) {
    adminNoticeService.insert(adminNoticeRequestDto);
  }

  @Override
  public void updateAdminNotice(String noticeId, AdminNoticeRequestDto adminNoticeRequestDto) {
    adminNoticeService.update(adminNoticeRequestDto, noticeId);
  }
}
