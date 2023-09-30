package com.laplace.api.common.model.db;

import com.laplace.api.common.constants.DBTables;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = DBTables.VERIFICATION_TOKENS)
public class VerificationToken implements Serializable {

  @Id
  @Column(name = "token")
  private String token;

  @Column(name = "user_id", nullable = false)
  private Integer userId;

  @Column(name = "created_on", nullable = false)
  private ZonedDateTime createdOn;

  @Column(name = "expiration_time", nullable = false)
  private Long expirartionTime;

  @Column(name = "app_type")
  private Integer appType;

  @Column(name = "token_type")
  private Integer tokenType;

  @Column(name = "context")
  private String context;

  @Column(name = "authentication_type", nullable = false)
  private Integer authenticationType;
}
