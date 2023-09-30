package com.laplace.api.cms.converter.response;

import com.laplace.api.cms.core.dto.CompanyInfoResponse;
import com.laplace.api.common.model.db.CompanyInfo;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CompanyInfoConverter implements Converter<CompanyInfo, CompanyInfoResponse> {

  private ModelMapper mapper;

  private CompanyInfoConverter() {
    this.mapper = new ModelMapper();
  }

  @Override
  public CompanyInfoResponse convert(CompanyInfo source) {
    return mapper.map(source, CompanyInfoResponse.class);
  }
}
