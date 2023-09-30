package com.laplace.api.common.dto.request;

import com.laplace.api.common.constants.ErrorCode;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class DocumentUploadDto {

    @NotEmpty(message = ErrorCode.INVALID_ARGUMENT)
    private String fileIdFront;

    private String fileIdBack;
}
