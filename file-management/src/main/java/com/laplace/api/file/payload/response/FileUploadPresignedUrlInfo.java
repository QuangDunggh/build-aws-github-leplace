package com.laplace.api.file.payload.response;

import com.laplace.api.common.constants.enums.AppType;
import com.laplace.api.common.constants.enums.FileType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FileUploadPresignedUrlInfo {
  FileType fileType;
  Long fileSize;
  String fileName;
  String fileContentType;
  String accessId;
  Integer uploadedBy;
  AppType appType;

  public static FileUploadPresignedUrlInfo validate(FileUploadPresignedUrlInfo fileUploadInfo) {
    requireNonNull(fileUploadInfo.getFileName(), "fileName");
    requireNonNull(fileUploadInfo.getFileSize(), "fileSize");
    requireNonNull(fileUploadInfo.getFileContentType(), "fileContentType");
    requireNonNull(fileUploadInfo.getUploadedBy(), "uploadedBy");
    requireNonNull(fileUploadInfo.getFileType(), "fileType");

    return fileUploadInfo;
  }

  private static <T> T requireNonNull(T obj, String keyName) {
    if (obj == null) {
      throw new IllegalArgumentException(keyName + ": is blank in " + FileUploadPresignedUrlInfo.class.getSimpleName());
    } else {
      return obj;
    }
  }
}
