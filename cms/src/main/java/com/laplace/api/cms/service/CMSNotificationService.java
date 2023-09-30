package com.laplace.api.cms.service;


import com.laplace.api.common.dto.request.AdminNoticeRequestDto;
import com.laplace.api.common.dto.response.AdminNoticeBasicResponseDTO;
import com.laplace.api.common.dto.response.AdminNoticeDetailsResponseDTO;
import com.laplace.api.common.util.PageableResponseDTO;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CMSNotificationService {

  List<AdminNoticeBasicResponseDTO> getAdminNotices(Integer month, Integer year,
      Pageable pageable);

  AdminNoticeDetailsResponseDTO getAdminNoticeDetails(String notification);

  void saveAdminNotice(AdminNoticeRequestDto notificationRequestDto);

  void updateAdminNotice(String noticeId, AdminNoticeRequestDto adminNoticeRequestDto);
}
