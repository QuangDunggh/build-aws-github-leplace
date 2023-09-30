package com.laplace.api.common.constants.enums;

public enum MeasurementType {

  LEG(1, "cm"),
  CHEST(2, "cm"),
  AROUND_THE_HEM(3, "cm"),
  NECK(4, "cm"),
  SHOULDER_WIDTH(5, "cm"),
  SLEEVE(6, "cm"),
  INSEAM(7, "cm"),
  HIP(8, "cm"),
  HEIGHT(9, "cm"),
  WEIGHT(10, "kg"),
  WAIST(11, "cm");

  private int val;
  private String unit;

  MeasurementType(int val, String unit) {
    this.val = val;
    this.unit = unit;
  }

  public String getUnit() {
    return unit;
  }
}
