package com.laplace.api.common.repository.db;

import com.laplace.api.common.model.db.AdminNotice;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminNoticeRepository extends JpaRepository<AdminNotice, String> {

  List<AdminNotice> findByCreatedOnBetweenOrderByCreatedOnDesc(ZonedDateTime startTime,
      ZonedDateTime endTime);
}
