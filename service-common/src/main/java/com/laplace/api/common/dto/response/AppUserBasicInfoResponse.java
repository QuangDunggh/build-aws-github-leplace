package com.laplace.api.common.dto.response;

import com.laplace.api.common.dto.BodyMeasurementDto;
import com.laplace.api.common.model.db.Profession;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppUserBasicInfoResponse {

  private String firstName;
  private String familyName;
  private String name;
  private Long birthDate;
  private String phoneNumber;
  private String profileImage;
  private List<BodyMeasurementDto> bodyMeasurements;
  private Integer shopId;
}
