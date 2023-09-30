package com.laplace.api.file.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.laplace.api.file.payload.request.FileUploadPresignedUrlRequest;
import com.laplace.api.file.payload.request.FileUploadRequest;
import com.laplace.api.file.payload.request.MultiFileUploadPresignedUrlRequest;
import com.laplace.api.file.payload.response.FileUploadDetails;
import java.util.Map;

public interface FileUploadService {

  FileUploadDetails fileUpload(FileUploadRequest fileUploadRequest);

  JsonNode getPresignedUrl(FileUploadPresignedUrlRequest presignedUrlRequest)
      throws JsonProcessingException;

  Map<String, FileUploadDetails> getBatchPreSignedUrl(MultiFileUploadPresignedUrlRequest request);

}
