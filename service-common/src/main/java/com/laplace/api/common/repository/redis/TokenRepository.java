package com.laplace.api.common.repository.redis;

import com.laplace.api.common.constants.enums.AppType;
import com.laplace.api.common.model.redis.Token;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<Token, String> {

  List<Token> findByUserId(Integer userId);
  List<Token> findByUserIdAndAppType(Integer userId, AppType appType);

}
