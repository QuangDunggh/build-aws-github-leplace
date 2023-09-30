package com.laplace.api.common.model.db;

import com.laplace.api.common.constants.DBTables;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Data
@Table(name = DBTables.USER_PROFILES)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CMSUserProfileModel implements Serializable {

  private static final long serialVersionUID = 8960415508897810218L;

  @Id
  @Column(name = "user_id")
  private Integer userId;

  @Column(name = "profile_image_url")
  private String profileImageUrl;

  @Column(name = "favicon_image_url")
  private String faviconImageUrl;

  @Column(name = "name")
  private String name;

  @Column(name = "is_notification_enable")
  private boolean isNotificationEnable;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "notificationEmailPK.userId",
      orphanRemoval = true)
  @Builder.Default
  private Set<NotificationEmailModel> notificationEmailModels = new LinkedHashSet<>();

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userId", insertable = false, updatable = false)
  private AdminUser user;

  public void addNotificationEmailModel(NotificationEmailModel notificationEmailModel) {
    notificationEmailModels.add(notificationEmailModel);
  }

  public void removeNotificationEmailModel(NotificationEmailModel notificationEmailModel) {
    notificationEmailModels.remove(notificationEmailModel);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(userId).build();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof CMSUserProfileModel)) {
      return false;
    }
    return new EqualsBuilder().append(this.userId, ((CMSUserProfileModel) obj).userId).isEquals();
  }
}
