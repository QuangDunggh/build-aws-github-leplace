package com.laplace.api.file.payload.request;

import com.laplace.api.common.constants.ErrorCode;
import com.laplace.api.common.constants.enums.AppType;
import com.laplace.api.common.constants.enums.FileType;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileUploadRequest {

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private FileType fileType;
  private MultipartFile file;
  private String accessId;
  private Integer userId;
  private AppType appType;
}
