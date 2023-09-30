package com.laplace.api.common.repository.db;

import com.laplace.api.common.model.db.CMSUserProfileModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileSettingsRepository extends JpaRepository<CMSUserProfileModel, Integer> {

  List<CMSUserProfileModel> findAllByUserIdInOrderByUserId(List<Integer> clientIdList);
}
