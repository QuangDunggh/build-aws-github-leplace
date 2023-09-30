package com.laplace.api.file.service.impl;


import static com.laplace.api.common.util.LaplaceResponseUtil.returnApplicationException;

import com.laplace.api.common.constants.enums.FileType;
import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.dto.FileInfo;
import com.laplace.api.common.service.s3.LaplaceS3Service;
import com.laplace.api.common.util.DateUtil;
import com.laplace.api.file.payload.response.FileUploadDetails;
import com.laplace.api.file.payload.response.FileUploadInfo;
import com.laplace.api.file.service.S3FileUploadService;
import com.laplace.api.file.utils.FileUtil;
import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class S3FileUploadServiceImpl implements S3FileUploadService {

  @Value("${spring.profiles.active}")
  private String activeProfile;

  private final LaplaceS3Service s3Service;

  @Autowired
  public S3FileUploadServiceImpl(LaplaceS3Service s3Service) {
    this.s3Service = s3Service;
  }

  @Override
  public String uploadAndGetUrl(FileUploadInfo uploadInfo) {
    return upload(uploadInfo).getFileUrl();
  }

  @Override
  public FileUploadDetails upload(FileUploadInfo uploadInfo) {
    FileUploadInfo fileUploadInfo = FileUploadInfo.validate(uploadInfo);

    switch (fileUploadInfo.getFileType()) {
      case PROFILE_IMAGE:
        try {
          return uploadMultiPartImageFile(fileUploadInfo);
        } catch (Exception e) {
          log.error("file error: " + e.getMessage());
          throw returnApplicationException(ResultCodeConstants.FILE_UPLOAD_FAILED);
        }
      default:
        throw returnApplicationException(ResultCodeConstants.FILE_UPLOAD_FAILED);
    }
  }

  private FileUploadDetails uploadMultiPartImageFile(FileUploadInfo fileUploadInfo) {
    MultipartFile multipartFile = fileUploadInfo.getMultipartFile();
    FileType fileType = fileUploadInfo.getFileType();
    String fileName = FileUtil.generateFileName(activeProfile, fileUploadInfo);

    FileUtil.checkFileSize(fileType, multipartFile.getSize());

    File file = FileUtil.convertMultiPartToFile(multipartFile);
    String fileExt = FileUtil.getFileExt(multipartFile);
    file = FileUtil.removeExifMetadata(file, new File("/tmp/out." + fileExt), fileExt);
    String fileUrl = s3Service.upload(file, fileName, fileType);
    file.delete();
    return FileUploadDetails.from(convertToFileInfo(fileUploadInfo, fileUrl));
  }

  private FileUploadDetails uploadMultiPartNonImageFile(FileUploadInfo fileUploadInfo) {
    MultipartFile multipartFile = fileUploadInfo.getMultipartFile();
    FileType fileType = fileUploadInfo.getFileType();
    String fileName = FileUtil.generateFileName(activeProfile, fileUploadInfo);

    FileUtil.checkFileSize(fileType, multipartFile.getSize());

    File file = FileUtil.convertMultiPartToFile(multipartFile);
    String fileUrl = s3Service.upload(file, fileName, fileType);
    file.delete();
    return FileUploadDetails.from(convertToFileInfo(fileUploadInfo, fileUrl));
  }

  private FileInfo convertToFileInfo(FileUploadInfo info, String fileUrl) {
    MultipartFile multipartFile = info.getMultipartFile();

    return FileInfo.builder()
        .fileType(info.getFileType())
        .fileSize(multipartFile.getSize())
        .fileName(multipartFile.getOriginalFilename())
        .contentType(multipartFile.getContentType())
        .fileUrl(fileUrl)
        .uploadedBy(info.getUploadedBy())
        .uploadedOn(DateUtil.timeNow())
        .appType(info.getAppType())
        .build();
  }

}
