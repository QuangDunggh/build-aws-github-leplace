package com.laplace.api.common.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;


@Entity(name = "states")
@Getter
@Setter
public class State {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, updatable = false)
  private Integer id;

  @Column(name = "name")
  private String name;

  @Column(name = "code")
  private String code;
}
