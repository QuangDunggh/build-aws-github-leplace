package com.laplace.api.common.model.db;

import static com.laplace.api.common.constants.ApplicationConstants.VALID_COMPANY_NAME;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.DBTables;
import com.laplace.api.common.constants.ErrorCode;
import com.laplace.api.common.constants.TextLength;
import com.laplace.api.common.validators.groups.CompanyInfoValidationGroup.Affiliate;
import com.laplace.api.common.validators.groups.CompanyInfoValidationGroup.Package;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = DBTables.COMPANY_INFO)
@DynamicUpdate
public class CompanyInfo implements Serializable {

  private static final long serialVersionUID = 8960415508897810218L;

  @Id
  @Column(name = "email", nullable = false, length = 100)
  @Email(regexp = ApplicationConstants.VALID_EMAIL_ADDRESS_REGEX, groups = {Package.class,
      Affiliate.class})
  private String email;

  @Column(name = "name", nullable = false, length = 100)
  @NotEmpty
  @Pattern(regexp = VALID_COMPANY_NAME, message = ErrorCode.INVALID_NAME_PATTERN, groups = {
      Package.class,
      Affiliate.class})
  @Length(min = TextLength.COMPANY_NAME_MIN, max = TextLength.COMPANY_NAME_MAX, message = ErrorCode.NAME_TOO_LONG, groups = {
      Package.class,
      Affiliate.class})
  private String name;

  @Column(name = "role", nullable = false)
  private Integer roleId;

  @Column(name = "site_url", length = 100)
  private String siteUrl;

  @Column(name = "api_end_point", length = 100)
  @NotEmpty(groups = Package.class)
  private String apiEndPoint;

  @Column(name = "api_client", length = 100)
  @NotEmpty(groups = Package.class)
  private String apiClient;

  @Column(name = "api_credential", length = 100)
  @NotEmpty(groups = Package.class)
  private String apiCredential;

  @Column(name = "api_key")
  @NotEmpty(groups = Package.class)
  private String apiKey;

  @Column(name = "last_updated_on")
  private ZonedDateTime lastUpdatedTime;

  @Column(name = "is_invited", nullable = false)
  private boolean invited;

  @Column(name = "email_domain", length = 100)
  private String emailDomain;

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(getEmail()).build();
  }

  @Override
  public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof CompanyInfo)) {
			return false;
		}
    return new EqualsBuilder().append(this.getEmail(), ((CompanyInfo) obj).getEmail()).isEquals();
  }
}
