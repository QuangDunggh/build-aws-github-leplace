package com.laplace.api.web.controller;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.Languages;
import com.laplace.api.common.constants.enums.ResponseType;
import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.web.constants.APIEndPoints;
import com.laplace.api.web.core.bean.InquiryBean;
import com.laplace.api.web.service.InquiryService;
import java.util.Collections;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIEndPoints.INQUIRY)
public class InquiryController {

  private final InquiryService inquiryService;

  @Autowired
  public InquiryController(InquiryService inquiryService) {
    this.inquiryService = inquiryService;
  }

  @PostMapping
  public BaseResponse postInquiry(@Valid @RequestBody InquiryBean inquiryBean) {
    inquiryService.postInquiry(inquiryBean, Languages.JAPANESE);
    return BaseResponse.builder()
        .responseType(ResponseType.RESULT)
        .message(Collections.singleton(ApplicationConstants.OK_MSG))
        .code(ApplicationConstants.SUCCESS_CODE)
        .build();
  }
}
