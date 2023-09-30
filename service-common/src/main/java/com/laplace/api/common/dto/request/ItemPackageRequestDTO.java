package com.laplace.api.common.dto.request;

import static com.laplace.api.common.constants.ApplicationConstants.ITEM_PACKAGE_MAX_SIZE;

import com.laplace.api.common.constants.ErrorCode;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

@Data
public class ItemPackageRequestDTO {

  @JsonIgnore
  private Integer userId;
  @JsonIgnore
  private String email;
  @JsonIgnore
  private String packageId;
  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private Long estimatedShippingDate;
  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private Integer addressId;
  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  @NotEmpty(message = ErrorCode.INVALID_ARGUMENT)
  @Size(max = ITEM_PACKAGE_MAX_SIZE, message = ErrorCode.INVALID_ARGUMENT)
  private List<@Valid ItemRequestDto> items;
}
