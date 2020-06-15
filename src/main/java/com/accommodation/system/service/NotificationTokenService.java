package com.accommodation.system.service;

import com.accommodation.system.entity.model.NotificationTokenInfo;

public interface NotificationTokenService {
    void saveTokenFirebase(NotificationTokenInfo notificationTokenInfo);

}
