package com.laplace.api.security.helper;

import static com.laplace.api.common.constants.ApplicationConstants.LAPLACE_LAMBDA_ACCESS_KEY;

import com.laplace.api.common.configuration.aws.LaplaceSecretsManager;
import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.util.LaplaceResponseUtil;
import com.laplace.api.common.util.UserContext;
import com.laplace.api.security.model.RivisionMaster;
import java.io.IOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.RevisionListener;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class MasterRevisionListener implements RevisionListener {

  @Autowired
  private AuthenticationFacade authenticationFacade;
  @Autowired
  private LaplaceSecretsManager secretsManager;

  @Override
  public void newRevision(Object revisionEntity) {
    RivisionMaster rev = (RivisionMaster) revisionEntity;
    Optional<UserContext> userContext = authenticationFacade.getUserContext();
    if (userContext.isPresent()) {
      rev.setRevisionBy(userContext.get().getUserId());
    } else {
      try {
        if (StringUtils.equals(MDC.get(LAPLACE_LAMBDA_ACCESS_KEY),
            secretsManager.getSecretsManagerKey().getLambdaKey())) {
          rev.setRevisionBy(ApplicationConstants.LAMBDA_USER);
        } else {
          LaplaceResponseUtil.throwApplicationException(ResultCodeConstants.USER_NOT_EXISTS);
        }
      } catch (IOException e) {
        log.error(e.getLocalizedMessage(),e);
      }
    }
  }
}
