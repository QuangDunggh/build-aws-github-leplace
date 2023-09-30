package com.laplace.api.common.model.redis;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.enums.AppType;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Data
@RedisHash(value = "Token", timeToLive = ApplicationConstants.EIGHT_HOUR_IN_SECONDS)
public class Token implements Serializable {

  private String id;
  private String jwtToken;
  private Integer expiredAfter;
  private Date createdAt;
  @Indexed
  private Integer userId;
  @Indexed
  private AppType appType;
  private Boolean profileComplete;
  private Boolean isActive;
}
