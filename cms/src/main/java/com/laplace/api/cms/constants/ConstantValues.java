package com.laplace.api.cms.constants;

import com.laplace.api.common.constants.enums.RequestStatus;
import java.util.Arrays;
import java.util.List;

public class ConstantValues {

  public static final List<RequestStatus> PUBLISH_REQUEST_STATUS = Arrays
      .asList(RequestStatus.PUBLISHED, RequestStatus.PUBLISH_REQUESTED,
          RequestStatus.PUBLISH_REQUEST_EXPIRED, RequestStatus.PUBLISH_REQUEST_REJECTED);

  public static final List<RequestStatus> BORROW_REQUEST_STATUS_ENUMS = Arrays
      .asList(RequestStatus.BORROWED,
          RequestStatus.BORROW_REQUESTED, RequestStatus.BORROW_REQUEST_EXPIRED,
          RequestStatus.BORROW_REQUEST_REJECTED);

  public static final List<String> BORROW_REQUEST_STATUS = Arrays
      .asList(RequestStatus.BORROWED.name(),
          RequestStatus.BORROW_REQUESTED.name(), RequestStatus.BORROW_REQUEST_EXPIRED.name(),
          RequestStatus.BORROW_REQUEST_REJECTED.name());
}
