package com.laplace.api.file.service;


import com.laplace.api.file.payload.response.FileUploadDetails;
import com.laplace.api.file.payload.response.FileUploadPresignedUrlInfo;

public interface S3FileUploadPresignedUrlService {

  FileUploadDetails getPresignedUrl(FileUploadPresignedUrlInfo fileUploadInfo);

  FileUploadDetails fileUploadDataValidator(FileUploadPresignedUrlInfo fileUploadInfo);
}
