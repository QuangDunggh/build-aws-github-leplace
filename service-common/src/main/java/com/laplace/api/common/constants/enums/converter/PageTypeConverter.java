package com.laplace.api.common.constants.enums.converter;

import com.laplace.api.common.constants.enums.PageType;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class PageTypeConverter implements AttributeConverter<PageType, Integer> {

  @Override
  public Integer convertToDatabaseColumn(PageType pageType) {
    return pageType.getType();
  }

  @Override
  public PageType convertToEntityAttribute(Integer dbData) {
    return PageType.getPageTypeFromVal(dbData);
  }
}
