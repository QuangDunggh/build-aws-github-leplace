package com.laplace.api.common.repository.db.queries;

public final class NativeQueries {

  public static final String ACTIVE_GOODS_PARTITION_BY_CLIENT_ID =
      "SELECT * FROM (SELECT *, ROW_NUMBER() OVER " +
          "(PARTITION BY client_id ORDER BY activate_date DESC) AS row_num " +
          "FROM content_activation_status WHERE content_type = 'GOODS' AND is_active = 1 AND is_archived = 0 "
          +
          "AND is_deleted = 0 AND is_hidden = 0 AND is_rental_available = 1 AND closed_at > UTC_TIMESTAMP() ) t "
          +
          "WHERE row_num <= 3;";
}
