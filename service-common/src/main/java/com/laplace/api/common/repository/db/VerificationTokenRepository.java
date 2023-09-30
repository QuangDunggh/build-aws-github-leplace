package com.laplace.api.common.repository.db;

import com.laplace.api.common.model.db.VerificationToken;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, String> {

  void deleteByToken(@Param("token") String token);

  List<VerificationToken> findAllByUserIdAndAppTypeAndTokenType(Integer userId, Integer appType,
      Integer tokenType);
}
