package com.laplace.api.web.core.bean;

import javax.validation.constraints.Email;
import lombok.Data;

@Data
public class EmailUpdateRequest {

  @Email
  private String currentEmail;

  @Email
  private String newEmail;
}
