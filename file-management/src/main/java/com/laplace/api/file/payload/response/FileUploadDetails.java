package com.laplace.api.file.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.laplace.api.common.constants.enums.FileType;
import com.laplace.api.common.dto.FileInfo;
import com.laplace.api.common.util.DateUtil;
import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
public class FileUploadDetails implements Serializable {

  private FileType fileType;
  private Long fileSize;
  private String fileName;
  private String contentType;
  private String fileBucket;
  private String fileKey;
  private String fileUrl;
  private String presignedUrl;
  private Integer uploadedBy;
  private Long uploadedOn;

  public static FileUploadDetails from(FileInfo fileInfo) {
    return from(fileInfo, null);
  }

  public static FileUploadDetails from(FileInfo fileInfo, String presignedUrl) {
    return FileUploadDetails.builder()
        .fileType(fileInfo.getFileType())
        .fileSize(fileInfo.getFileSize())
        .fileName(fileInfo.getFileName())
        .contentType(fileInfo.getContentType())
        .fileUrl(fileInfo.getFileUrl())
        .presignedUrl(presignedUrl)
        .uploadedBy(fileInfo.getUploadedBy())
        .uploadedOn(DateUtil.toEpochMilli(fileInfo.getUploadedOn()))
        .build();
  }
}
