package com.laplace.api.cms.core.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.laplace.api.common.util.JsonZoneDateTimeDeserializer;
import com.laplace.api.common.util.JsonZoneDateTimeSerializer;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyInfoResponse implements Serializable {

  private String email;

  private String name;

  private String siteUrl;

  private boolean isInvited;

  @JsonSerialize(using = JsonZoneDateTimeSerializer.class)
  @JsonDeserialize(using = JsonZoneDateTimeDeserializer.class)
  private ZonedDateTime lastUpdatedTime;

  public int hashCode() {
    return new HashCodeBuilder().append(email).build();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof CompanyInfoResponse)) {
      return false;
    }
    return new EqualsBuilder().append(this.email, ((CompanyInfoResponse) obj).email).isEquals();
  }
}
