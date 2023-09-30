package com.laplace.api.common.model.db;

import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stores")
@IdClass(StorePK.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Store {

  @Id
  @Column(name = "id")
  private Integer id;

  @Id
  @Column(name = "client_id")
  private Integer clientId;

  @Column(name = "name")
  private String name;

  @Column(name = "location")
  private String location;

  @Column(name = "city")
  private String city;

  @Column(name = "zip")
  private String zip;

  @Column(name = "state_id")
  private Integer stateId;

  @Column(name = "line1")
  private String line1;

  @Column(name = "line2")
  private String line2;

  @Column(name = "storage_id")
  private Integer storageId;

  @Column(name = "created_at")
  private Date createdAt;

  @Column(name = "updated_at")
  private Date updatedAt;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Store that = (Store) o;
    return Objects.equals(clientId, that.clientId) &&
        Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clientId, id);
  }

}