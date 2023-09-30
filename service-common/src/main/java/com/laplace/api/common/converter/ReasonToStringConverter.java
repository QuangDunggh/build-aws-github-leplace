package com.laplace.api.common.converter;

import com.laplace.api.common.constants.ApplicationConstants.StringUtils;
import com.laplace.api.common.constants.enums.WithdrawalReason;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.AttributeConverter;
import org.springframework.util.ObjectUtils;

public class ReasonToStringConverter implements AttributeConverter<List<WithdrawalReason>, String> {

  @Override
  public String convertToDatabaseColumn(List<WithdrawalReason> withdrawalReasons) {
    return ObjectUtils.isEmpty(withdrawalReasons) ? StringUtils.EMPTY_STRING
        : withdrawalReasons.stream().map(WithdrawalReason::name)
            .collect(Collectors.joining(StringUtils.COMMA));
  }

  @Override
  public List<WithdrawalReason> convertToEntityAttribute(String reasonAsString) {
    return ObjectUtils.isEmpty(reasonAsString) ? Collections.emptyList()
        : Arrays.stream(reasonAsString.split(StringUtils.COMMA)).map(WithdrawalReason::forName)
            .collect(Collectors.toList());
  }
}
