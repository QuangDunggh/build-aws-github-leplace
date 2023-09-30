package com.laplace.api.security.model;

import com.laplace.api.security.helper.MasterRevisionListener;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

@Entity
@RevisionEntity(MasterRevisionListener.class)
@Table(name = "revision_master")
public class RivisionMaster extends DefaultRevisionEntity {

  private Integer revisionBy;

  public Integer getRevisionBy() {
    return revisionBy;
  }

  public void setRevisionBy(Integer revisionBy) {
    this.revisionBy = revisionBy;
  }
}
