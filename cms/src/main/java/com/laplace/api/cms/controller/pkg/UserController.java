package com.laplace.api.cms.controller.pkg;


import static com.laplace.api.common.constants.enums.ResponseType.RESULT;

import com.laplace.api.cms.constants.APIEndPoints;
import com.laplace.api.cms.service.UserService;
import com.laplace.api.common.constants.enums.EntityType;
import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.security.marker.AccessControl;
import java.util.Collections;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping(APIEndPoints.APP_USER)
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/{id}")
  @AccessControl(EntityType.PACKAGE_ORDER)
  @ResponseStatus(HttpStatus.OK)
  public BaseResponse getAppUserDetails(@PathVariable("id") Integer userId) {
    return BaseResponse.builder()
        .responseType(RESULT)
        .message(Collections.singleton(HttpStatus.OK.getReasonPhrase()))
        .result(userService.getAppUserProfile(userId))
        .build();
  }
}
