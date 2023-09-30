package com.laplace.api.web.core.bean;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaymentBean {

  @Min(4)
  @Max(100)
  private String card;

  @NonNull
  @Min(50)
  @Max(9999999)
  private Integer amount;

  @Min(3)
  @Max(3)
  private String currency;

  @NonNull
  private Boolean capture;

}
