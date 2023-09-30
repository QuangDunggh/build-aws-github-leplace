package com.laplace.api.cms.service;

import com.laplace.api.cms.core.bean.ChangePasswordRequest;
import com.laplace.api.cms.core.bean.LoginRequestDto;
import com.laplace.api.cms.core.bean.SignUpRequest;
import com.laplace.api.cms.core.dto.LogInResponseDto;
import com.laplace.api.common.dto.request.ForgotPasswordRequest;
import com.laplace.api.common.dto.request.ResetPasswordRequest;

public interface CMSAuthService {

  LogInResponseDto login(LoginRequestDto loginRequestDto);

  void generateResetPasswordTokenAndSendMail(ForgotPasswordRequest request);

  void resetPassword(ResetPasswordRequest request);

  void signUp(SignUpRequest request);

  void changePassword(ChangePasswordRequest request, Integer userId);

  void logout(String token);
}
