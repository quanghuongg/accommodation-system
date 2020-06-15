package com.accommodation.system.service;

import com.accommodation.system.entity.model.NotificationMessage;

import java.io.IOException;

public interface NotificationService {
    void saveNotification(NotificationMessage notificationMessage);

    void sendBatchNotifications(NotificationMessage notificationMessages, boolean isSend) throws IOException;
}
