package com.laplace.api.cms.controller;

import static com.laplace.api.common.constants.ApplicationConstants.MAX_MONTH;
import static com.laplace.api.common.constants.ApplicationConstants.MIN_MONTH;
import static com.laplace.api.common.constants.ApplicationConstants.MIN_YEAR;

import com.laplace.api.cms.constants.APIEndPoints;
import com.laplace.api.cms.constants.CmsApplicationConstants.RequestParams;
import com.laplace.api.cms.service.CMSNotificationService;
import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.dto.request.AdminNoticeRequestDto;
import com.laplace.api.common.util.BaseResponse;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIEndPoints.ADMIN_NOTICE)
public class AdminNoticeController {

  private final CMSNotificationService adminNoticeService;

  @Autowired
  public AdminNoticeController(CMSNotificationService CMSNotificationService) {
    this.adminNoticeService = CMSNotificationService;
  }

  @GetMapping()
  public BaseResponse getAdminNotices(
      @RequestParam(RequestParams.MONTH) @Range(min = MIN_MONTH, max = MAX_MONTH) Integer month,
      @RequestParam(RequestParams.YEAR) @Min(MIN_YEAR) Integer year,
      @PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {
    return BaseResponse.create(adminNoticeService.getAdminNotices(month, year, pageable));
  }

  @GetMapping(APIEndPoints.PATH_VARIABLE_ID)
  public BaseResponse getAdminNoticeDetails(@PathVariable("id") String noticeId) {
    return BaseResponse.create(adminNoticeService.getAdminNoticeDetails(noticeId));
  }

  @PostMapping()
  public BaseResponse saveAdminNotice(
      @RequestBody @Valid AdminNoticeRequestDto adminNoticeRequestDto) {
    adminNoticeRequestDto.validate();
    adminNoticeService.saveAdminNotice(adminNoticeRequestDto);
    return BaseResponse.create(ApplicationConstants.CREATED_MSG);
  }

  @PutMapping(APIEndPoints.PATH_VARIABLE_ID)
  public BaseResponse updateAdminNotice(@PathVariable("id") String noticeId,
      @RequestBody @Valid AdminNoticeRequestDto adminNoticeRequestDto) {
    adminNoticeRequestDto.validate();
    adminNoticeService.updateAdminNotice(noticeId, adminNoticeRequestDto);
    return BaseResponse.create(ApplicationConstants.UPDATED_MSG);
  }
}
