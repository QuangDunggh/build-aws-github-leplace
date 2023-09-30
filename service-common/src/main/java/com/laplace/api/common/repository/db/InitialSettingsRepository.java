package com.laplace.api.common.repository.db;

import com.laplace.api.common.model.db.InitialSettings;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InitialSettingsRepository extends CrudRepository <InitialSettings, Integer>{

}
