package com.laplace.api.common.model.db.primarykey;


import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPackageAccessTokenPK implements Serializable {

  @Column(name = "user_id")
  private Integer userId;
  @Column(name = "client_id")
  private Integer clientId;

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(userId)
        .append(clientId).hashCode();
  }

  @Override
  public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof UserPackageAccessTokenPK)) {
			return false;
		}
    UserPackageAccessTokenPK that = (UserPackageAccessTokenPK) obj;
    return new EqualsBuilder().append(this.userId, that.userId)
        .append(this.clientId, that.clientId).isEquals();
  }
}
