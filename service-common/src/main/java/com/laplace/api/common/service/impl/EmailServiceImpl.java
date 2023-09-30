package com.laplace.api.common.service.impl;

import com.laplace.api.common.service.EmailService;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Configuration
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender emailSender;

  @Autowired
  public EmailServiceImpl(JavaMailSender emailSender) {
    this.emailSender = emailSender;
  }

  @Async("taskExecutor")
  @Override
  public void sendMail(String to, String from, String fromName, String subject, String body,
      String attachmentFilename, File file)
      throws MessagingException, UnsupportedEncodingException {
    MimeMessage mimeMessage = emailSender.createMimeMessage();
    mimeMessage.setFrom(new InternetAddress(from, fromName));
    mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));

    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
        MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
        StandardCharsets.UTF_8.name());
    helper.setSubject(subject);
    helper.setText(body, true);

    if (StringUtils.isNotBlank(attachmentFilename) && file != null) {
      helper.addAttachment(attachmentFilename, file);
    }

    emailSender.send(mimeMessage);
  }

  @Async("taskExecutor")
  @Override
  public void sendMail(String to, String from, String fromName, String subject, String body)
      throws MessagingException, UnsupportedEncodingException {
    sendMail(to, from, fromName, subject, body, null, null);
  }

  @Override
  public void recover(RuntimeException e) {
    log.error("Mail sending error occurred on " + e.getClass().getName());
  }
}
