package com.laplace.api.file.payload.response;

import com.laplace.api.common.constants.enums.AppType;
import com.laplace.api.common.constants.enums.FileType;
import lombok.Builder;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

@Value
@Builder
public class FileUploadInfo {

  FileType fileType;
  MultipartFile multipartFile;
  String accessId;
  Integer uploadedBy;
  AppType appType;

  public static FileUploadInfo validate(FileUploadInfo fileUploadInfo) {
    requireNonNull(fileUploadInfo.getMultipartFile(), "multipartFile");
    requireNonNull(fileUploadInfo.getUploadedBy(), "uploadedBy");
    requireNonNull(fileUploadInfo.getFileType(), "fileType");
    return fileUploadInfo;
  }

  private static <T> T requireNonNull(T obj, String keyName) {
    if (obj == null) {
      throw new IllegalArgumentException(
          keyName + ": is blank in " + FileUploadInfo.class.getSimpleName());
    } else {
      return obj;
    }
  }
}
