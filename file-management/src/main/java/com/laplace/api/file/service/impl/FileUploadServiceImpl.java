package com.laplace.api.file.service.impl;

import static com.laplace.api.common.util.LaplaceResponseUtil.returnApplicationException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.laplace.api.common.configuration.aws.LaplaceSecretsManager;
import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.file.payload.request.FileUploadDataValidatorRequest;
import com.laplace.api.file.payload.request.FileUploadPresignedUrlRequest;
import com.laplace.api.file.payload.request.FileUploadRequest;
import com.laplace.api.file.payload.request.MultiFileUploadPresignedUrlRequest;
import com.laplace.api.file.payload.response.FileUploadDetails;
import com.laplace.api.file.service.FileUploadService;
import com.laplace.api.file.service.lambda.FileUploadLambdaService;
import java.io.IOException;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {

  private final FileUploadFacade fileUploadFacade;
  private final LaplaceSecretsManager secretsManager;
  private final FileUploadLambdaService lambdaService;

  @Autowired
  public FileUploadServiceImpl(
      FileUploadFacade fileUploadFacade,
      LaplaceSecretsManager secretsManager,
      FileUploadLambdaService lambdaService) {
    this.fileUploadFacade = fileUploadFacade;
    this.secretsManager = secretsManager;
    this.lambdaService = lambdaService;
  }

  @Override
  public FileUploadDetails fileUpload(FileUploadRequest request) {
    return fileUploadFacade.upload(request);
  }

  @Override
  public JsonNode getPresignedUrl(FileUploadPresignedUrlRequest request)
      throws JsonProcessingException {
    FileUploadDetails details =  fileUploadFacade.fileUploadDataValidator(request);
    return lambdaService.generatePresignedUrl(details);
  }

  @Override
  public Map<String, FileUploadDetails> getBatchPreSignedUrl(
      MultiFileUploadPresignedUrlRequest request) {

    return request.getFiles()
        .stream()
        .map(uploadFile -> FileUploadPresignedUrlRequest
            .builder()
            .accessId(request.getAccessId())
            .appType(request.getAppType())
            .fileContentType(request.getFileContentType())
            .fileName(uploadFile.getFileName())
            .fileSize(uploadFile.getFileSize())
            .fileType(request.getFileType())
            .userId(request.getUserId())
            .build()
        ).map(fileUploadFacade::getPresignedUrl)
        .collect(
            Collectors.toMap(FileUploadDetails::getFileName, Function.identity(), (a, b) -> a));

  }
}
