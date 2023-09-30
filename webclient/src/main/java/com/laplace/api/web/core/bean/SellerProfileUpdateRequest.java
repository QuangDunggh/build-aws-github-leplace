package com.laplace.api.web.core.bean;

import lombok.Data;

@Data
public class SellerProfileUpdateRequest {

  private String userName;

  private String profileImage;

  private String sellClosetImage;

  private String sellClosetName;

  private Boolean shareFb;

  private Boolean shareTwitter;

}
