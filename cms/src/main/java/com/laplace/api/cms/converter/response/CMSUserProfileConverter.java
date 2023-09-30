package com.laplace.api.cms.converter.response;

import com.laplace.api.cms.core.dto.CMSUserProfileDto;
import com.laplace.api.common.model.db.CMSUserProfileModel;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CMSUserProfileConverter implements Converter<CMSUserProfileModel, CMSUserProfileDto> {

  private ModelMapper mapper;

  private CMSUserProfileConverter() {
    this.mapper = new ModelMapper();
  }

  @Override
  public CMSUserProfileDto convert(CMSUserProfileModel source) {
    return mapper.map(source, CMSUserProfileDto.class);
  }
}
