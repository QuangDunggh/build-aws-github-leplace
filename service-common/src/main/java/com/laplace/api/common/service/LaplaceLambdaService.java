package com.laplace.api.common.service;

import com.laplace.api.common.model.db.Notification;

public interface LaplaceLambdaService {

  void itemDiscountNotifyHandler(Notification data);

  void adminNoticeHandler();
}
