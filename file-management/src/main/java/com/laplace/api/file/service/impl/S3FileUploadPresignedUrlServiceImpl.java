package com.laplace.api.file.service.impl;


import static com.laplace.api.common.util.LaplaceResponseUtil.returnApplicationException;

import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.dto.FileInfo;
import com.laplace.api.common.service.s3.LaplaceS3Service;
import com.laplace.api.common.util.DateUtil;
import com.laplace.api.file.payload.response.FileUploadDetails;
import com.laplace.api.file.payload.response.FileUploadPresignedUrlInfo;
import com.laplace.api.file.service.S3FileUploadPresignedUrlService;
import com.laplace.api.file.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class S3FileUploadPresignedUrlServiceImpl implements S3FileUploadPresignedUrlService {

  @Value("${spring.profiles.active}")
  private String activeProfile;

  private final LaplaceS3Service s3Service;

  @Autowired
  public S3FileUploadPresignedUrlServiceImpl(LaplaceS3Service s3Service) {
    this.s3Service = s3Service;
  }

  @Override
  public FileUploadDetails getPresignedUrl(FileUploadPresignedUrlInfo uploadInfo) {
    FileUploadPresignedUrlInfo info = FileUploadPresignedUrlInfo.validate(uploadInfo);
    try {
      return generatePresignedUrl(info, true);
    } catch (Exception e) {
      log.error("file error: " + e.getMessage());
      throw returnApplicationException(
          ResultCodeConstants.FILE_UPLOAD_PRE_SIGNED_URL_GENERATE_FAILED);
    }
  }

  @Override
  public FileUploadDetails fileUploadDataValidator(FileUploadPresignedUrlInfo uploadInfo) {
    FileUploadPresignedUrlInfo info = FileUploadPresignedUrlInfo.validate(uploadInfo);
    try {
      return validate(info, true);
    } catch (Exception e) {
      log.error("file error: " + e.getMessage());
      throw returnApplicationException(ResultCodeConstants.INVALID_ARGUMENT);
    }
  }

  private FileUploadDetails generatePresignedUrl(FileUploadPresignedUrlInfo info,
      boolean checkFileSize) {
    if (checkFileSize) {
      FileUtil.checkFileSize(info.getFileType(), info.getFileSize());
    }

    String fileName = FileUtil.generateFileName(activeProfile, info);
    String fileContentType = info.getFileContentType();
    Pair<String, String> pair = s3Service
        .generateUploadPresignedUrl(fileName, fileContentType);
    String s3FileUrl = pair.getKey();
    String uploadPresignedUrl = pair.getRight();
    return FileUploadDetails.from(convert(info, s3FileUrl), uploadPresignedUrl);
  }

  private FileUploadDetails validate(FileUploadPresignedUrlInfo info,
      boolean checkFileSize) {
    if (checkFileSize) {
      FileUtil.checkFileSize(info.getFileType(), info.getFileSize());
    }

    String fileName = FileUtil.generateFileName(activeProfile, info);
    String s3FileUrl = s3Service.getS3FileUrl(fileName);
    FileUploadDetails fileUploadDetails = FileUploadDetails
        .from(convert(info, s3FileUrl), StringUtils.EMPTY);
    fileUploadDetails.setFileBucket(s3Service.getMediaBucketName());
    fileUploadDetails.setFileKey(fileName);
    return fileUploadDetails;
  }

  private FileInfo convert(FileUploadPresignedUrlInfo info, String fileUrl) {
    return FileInfo.builder()
        .fileType(info.getFileType())
        .fileSize(info.getFileSize())
        .fileName(info.getFileName())
        .contentType(info.getFileContentType())
        .fileUrl(fileUrl)
        .uploadedBy(info.getUploadedBy())
        .uploadedOn(DateUtil.timeNow())
        .accessId(info.getAccessId())
        .build();
  }
}
