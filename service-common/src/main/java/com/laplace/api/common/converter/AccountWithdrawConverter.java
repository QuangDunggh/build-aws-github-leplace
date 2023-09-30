package com.laplace.api.common.converter;

import com.laplace.api.common.dto.request.AccountWithdrawRequestDto;
import com.laplace.api.common.model.db.AccountWithdrawReason;
import com.laplace.api.common.util.DateUtil;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

@Component
public class AccountWithdrawConverter implements
    Converter<AccountWithdrawRequestDto, AccountWithdrawReason> {

  @Override
  public AccountWithdrawReason convert(AccountWithdrawRequestDto source) {
    AccountWithdrawReason target = new AccountWithdrawReason();
    target.setUserId(source.getUserId());
    target.setReason(source.getReason());
    target.setDetailReason(source.getDetailsReason());
    target.setCreatedOn(DateUtil.timeNow());
    target.setLastUpdatedOn(DateUtil.timeNow());
    target.setLastUpdatedBy(source.getUserId());
    return target;
  }
}
