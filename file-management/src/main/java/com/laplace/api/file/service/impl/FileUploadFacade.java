package com.laplace.api.file.service.impl;


import com.laplace.api.common.constants.enums.FileType;
import com.laplace.api.common.service.AppUserService;
import com.laplace.api.file.payload.request.FileUploadDataValidatorRequest;
import com.laplace.api.file.payload.request.FileUploadPresignedUrlRequest;
import com.laplace.api.file.payload.request.FileUploadRequest;
import com.laplace.api.file.payload.response.FileUploadDetails;
import com.laplace.api.file.payload.response.FileUploadInfo;
import com.laplace.api.file.payload.response.FileUploadPresignedUrlInfo;
import com.laplace.api.file.service.S3FileUploadPresignedUrlService;
import com.laplace.api.file.service.S3FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FileUploadFacade {

  private final S3FileUploadService fileUploadService;
  private final S3FileUploadPresignedUrlService presignedUrlService;

  @Autowired
  public FileUploadFacade(
      AppUserService appUserService,
      S3FileUploadService fileUploadService,
      S3FileUploadPresignedUrlService presignedUrlService) {
    this.fileUploadService = fileUploadService;
    this.presignedUrlService = presignedUrlService;
  }

  public FileUploadDetails upload(FileUploadRequest request) {
    FileType fileType = request.getFileType();

    FileUploadInfo info = FileUploadInfo.builder()
        .fileType(fileType)
        .multipartFile(request.getFile())
        .accessId(request.getAccessId())
        .uploadedBy(request.getUserId())
        .appType(request.getAppType())
        .build();

    return fileUploadService.upload(info);
  }

  public FileUploadDetails getPresignedUrl(FileUploadPresignedUrlRequest request) {
    FileUploadPresignedUrlInfo info = FileUploadPresignedUrlInfo.builder()
        .fileType(request.getFileType())
        .fileName(request.getFileName())
        .fileSize(request.getFileSize())
        .fileContentType(request.getFileContentType())
        .accessId(request.getAccessId())
        .uploadedBy(request.getUserId())
        .build();

    return presignedUrlService.getPresignedUrl(info);
  }

  public FileUploadDetails fileUploadDataValidator(FileUploadPresignedUrlRequest request) {

    FileUploadPresignedUrlInfo info = FileUploadPresignedUrlInfo.builder()
        .fileType(request.getFileType())
        .fileName(request.getFileName())
        .fileSize(request.getFileSize())
        .fileContentType(request.getFileContentType())
        .accessId(request.getAccessId())
        .uploadedBy(request.getUserId())
        .build();
    return presignedUrlService.fileUploadDataValidator(info);
  }
}
