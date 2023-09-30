package com.laplace.api.cms.core.dto;

import com.laplace.api.common.constants.enums.RequestStatus;
import java.util.List;
import lombok.Data;
import org.springframework.data.util.Pair;

@Data
public class NotificationDetailsResponseDto {

  private Integer id;
  private String title;
  private String stylistName;
  private List<Pair<String, Boolean>> contentUrl;
  private Long requestStart;
  private Long requestEnd;
  private RequestStatus status;
  private Long arrivalDate;
  private Long acceptDate;
  private String message;
  private String email;
  private Integer nType;
}
