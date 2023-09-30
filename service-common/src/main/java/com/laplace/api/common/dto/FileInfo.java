package com.laplace.api.common.dto;

import com.laplace.api.common.constants.enums.AppType;
import com.laplace.api.common.constants.enums.FileType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileInfo implements Serializable {

  private FileType fileType;
  private Long fileSize;
  private String fileName;
  private String contentType;
  private String fileUrl;
  private Integer uploadedBy;
  private ZonedDateTime uploadedOn;
  private String accessId;
  private AppType appType;

}
