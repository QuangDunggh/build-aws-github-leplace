package com.laplace.api.common.service;

import com.laplace.api.common.constants.ApplicationConstants;
import java.io.File;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

public interface EmailService {

  void sendMail(String to, String from, String fromName, String subject, String body,
      String attachmentFilename,
      File file) throws MessagingException, UnsupportedEncodingException;

  @Retryable(value = {MessagingException.class,
      UnsupportedEncodingException.class}, maxAttempts = ApplicationConstants.MAX_ATTEMPT, backoff = @Backoff(3000))
  void sendMail(String to, String from, String fromName, String subject, String body)
      throws MessagingException, UnsupportedEncodingException;

  @Recover
  public void recover(RuntimeException e);
}
