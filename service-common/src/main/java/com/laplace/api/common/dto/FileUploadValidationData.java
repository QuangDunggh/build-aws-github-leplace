package com.laplace.api.common.dto;

import com.laplace.api.common.constants.enums.AppType;
import com.laplace.api.common.constants.enums.FileType;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class FileUploadValidationData {
  AppType appType;
  Integer uploadedBy;
  String accessId;
  FileType fileType;
}
