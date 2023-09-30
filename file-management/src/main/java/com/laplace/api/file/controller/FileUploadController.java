package com.laplace.api.file.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.file.constants.APIEndPoints;
import com.laplace.api.file.payload.request.FileUploadDataValidatorRequest;
import com.laplace.api.file.payload.request.FileUploadPresignedUrlRequest;
import com.laplace.api.file.payload.request.FileUploadRequest;
import com.laplace.api.file.payload.request.MultiFileUploadPresignedUrlRequest;
import com.laplace.api.file.service.FileUploadService;
import com.laplace.api.security.helper.AuthenticationFacade;
import java.io.IOException;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@Slf4j
@RequestMapping(APIEndPoints.FILE_UPLOAD)
public class FileUploadController {

  private final FileUploadService fileUploadService;
  private final AuthenticationFacade authenticationFacade;

  @Autowired
  public FileUploadController(FileUploadService fileUploadService,
      AuthenticationFacade authenticationFacade) {
    this.fileUploadService = fileUploadService;
    this.authenticationFacade = authenticationFacade;
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public BaseResponse fileUpload(@ModelAttribute @Valid FileUploadRequest fileUploadRequest) {
    fileUploadRequest.setAccessId(authenticationFacade.getAccessId());
    fileUploadRequest.setUserId(authenticationFacade.getUserId());
    fileUploadRequest.setAppType(authenticationFacade.getAppType());
    return BaseResponse.create(fileUploadService.fileUpload(fileUploadRequest));
  }

  @PostMapping(APIEndPoints.PRE_SIGNED_URL)
  public BaseResponse getPresignedUrl(@RequestBody @Valid FileUploadPresignedUrlRequest request)
      throws JsonProcessingException {
    request.validate();
    request.setAccessId(authenticationFacade.getAccessId());
    request.setUserId(authenticationFacade.getUserId());
    request.setAppType(authenticationFacade.getAppType());
    return BaseResponse.create(fileUploadService.getPresignedUrl(request));
  }

  @PostMapping(APIEndPoints.BATCH_PRE_SIGNED_URL)
  public BaseResponse getMultipleFilesPresignedUrl(
      @RequestBody @Valid MultiFileUploadPresignedUrlRequest request) {
    request.validate();
    request.setAccessId(authenticationFacade.getAccessId());
    request.setUserId(authenticationFacade.getUserId());
    request.setAppType(authenticationFacade.getAppType());
    return BaseResponse.create(fileUploadService.getBatchPreSignedUrl(request));
  }
}
