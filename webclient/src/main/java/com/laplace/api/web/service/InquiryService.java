package com.laplace.api.web.service;

import com.laplace.api.common.model.db.Inquiry;
import com.laplace.api.common.repository.db.InquiryRepository;
import com.laplace.api.web.core.bean.InquiryBean;
import com.laplace.api.web.core.enums.InquiryType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InquiryService {

  private final InquiryRepository inquiryRepository;
  private final SendInquiryMailService sendInquiryMailService;

  @Autowired
  public InquiryService(InquiryRepository inquiryRepository,
      SendInquiryMailService sendInquiryMailService) {
    this.inquiryRepository = inquiryRepository;
    this.sendInquiryMailService = sendInquiryMailService;
  }

  public void postInquiry(InquiryBean inquiryBean, String lang) {
    InquiryType inquiryType = InquiryType.fromId(inquiryBean.getType());
    Inquiry inquiry = new Inquiry();
    BeanUtils.copyProperties(inquiryBean, inquiry);
    inquiryRepository.save(inquiry);
    sendInquiryMailService.send(inquiryBean.getEmail(), inquiryType, inquiryBean.getContent(), lang);
    sendInquiryMailService
        .sendMailToContactedUser(inquiryBean.getEmail(), inquiryType, inquiryBean,
            lang);
  }
}
