package com.laplace.api.common.dto.response;

import com.laplace.api.common.constants.ErrorCode;
import com.laplace.api.common.constants.enums.PageSection;
import com.laplace.api.common.validators.ValidEnum;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Section implements Serializable {

  @ValidEnum(message = ErrorCode.INVALID_PAGE_SECTION, enumClass = PageSection.class)
  private String sectionName;

  private String content;

  private MultipartFile file;
}
