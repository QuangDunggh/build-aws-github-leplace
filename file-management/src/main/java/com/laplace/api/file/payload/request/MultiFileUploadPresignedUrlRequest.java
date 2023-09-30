package com.laplace.api.file.payload.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.laplace.api.common.constants.enums.AppType;
import com.laplace.api.common.constants.enums.FileType;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

@Data
public class MultiFileUploadPresignedUrlRequest {

  private FileType fileType;
  private String fileContentType;
  private List<UploadFile> files;
  private String accessId;
  private Integer userId;
  private AppType appType;

  @JsonIgnore
  public void validate() {
    requireNonNull(getFileContentType(), "fileContentType");
    requireNonNull(getFileType(), "fileType");
    requireNonNull(getFiles(), "files");
  }

  private static <T> T requireNonNull(T obj, String keyName) {
    if (ObjectUtils.isEmpty(obj)) {
      throw new IllegalArgumentException(
          keyName + ": is blank in " + MultiFileUploadPresignedUrlRequest.class.getSimpleName());
    } else {
      return obj;
    }
  }

  @Data
  public static class UploadFile {
    private Long fileSize;
    private String fileName;
  }
}
