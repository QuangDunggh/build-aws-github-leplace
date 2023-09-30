package com.laplace.api.file.payload.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.laplace.api.common.constants.enums.AppType;
import com.laplace.api.common.constants.enums.FileType;
import lombok.Data;

@Data
public class FileUploadDataValidatorRequest {

  private String secretKey;
  private FileType fileType;
  private Long fileSize;
  private String fileName;
  private String fileContentType;
  private String accessId;
  private Integer userId;
  private AppType appType;

  @JsonIgnore
  public void validate() {
    requireNonNull(getSecretKey(), "secretKey");
    requireNonNull(getFileName(), "fileName");
    requireNonNull(getFileSize(), "fileSize");
    requireNonNull(getFileContentType(), "fileContentType");
    requireNonNull(getFileType(), "fileType");
  }

  private static <T> T requireNonNull(T obj, String keyName) {
    if (obj == null) {
      throw new IllegalArgumentException(
          keyName + ": is blank in " + FileUploadDataValidatorRequest.class.getSimpleName());
    } else {
      return obj;
    }
  }
}
