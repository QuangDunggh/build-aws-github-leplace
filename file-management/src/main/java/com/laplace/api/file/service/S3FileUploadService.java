package com.laplace.api.file.service;


import com.laplace.api.file.payload.response.FileUploadDetails;
import com.laplace.api.file.payload.response.FileUploadInfo;

public interface S3FileUploadService {

  String uploadAndGetUrl(FileUploadInfo fileUploadInfo);

  FileUploadDetails upload(FileUploadInfo fileUploadInfo);
}
