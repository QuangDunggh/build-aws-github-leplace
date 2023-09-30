package com.laplace.api.common.service;

import com.laplace.api.common.constants.enums.AppType;
import com.laplace.api.common.model.redis.Token;
import java.util.List;
import java.util.Optional;

public interface TokenService {

  void save(Token token);

  Optional<Token> findByToken(String token);

  List<Token> findByUserId(Integer userId);

  void deleteAll(List<Token> tokenList);

  void delete(Token token);

  List<Token> findByUserIdAndAppType(Integer userId, AppType appType);
}
