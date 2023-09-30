package com.laplace.api.cms.core.dto;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.util.Pair;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDto implements Serializable {

  private Integer id;
  private Integer requestId;
  private String status;
  private boolean readStatus;
  private String requestFrom;
  private String requestTo;
  private Integer notificationType;
  private Long requestedDate;
  private Long displayStart;
  private Long displayEnd;
  private String movieTitle;
  private String movieDescription;
  private List<Pair<String, Boolean>> videoContent;
  private String goodsName;
  private String productCode;
  private String goodsSize;
  private String goodsColor;
  private Integer goodsRentalPrice;
  private String coordinateTitle;
  private String stylistName;
  private List<Pair<String, Boolean>> images;
}