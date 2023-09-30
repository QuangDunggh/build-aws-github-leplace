package com.laplace.api.cms.converter.response;

import com.laplace.api.cms.core.dto.CMSUserProfileDto;
import com.laplace.api.common.model.db.CMSUserProfileModel;
import javax.annotation.Nonnull;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CMSUserProfileDtoConverter implements
    Converter<CMSUserProfileDto, CMSUserProfileModel> {

  private ModelMapper mapper;

  private CMSUserProfileDtoConverter() {
    this.mapper = new ModelMapper();
  }

  @Override
  public @Nonnull
  CMSUserProfileModel convert(@Nonnull CMSUserProfileDto source) {
    return mapper.map(source, CMSUserProfileModel.class);
  }
}
