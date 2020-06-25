package com.accommodation.system.service;

import com.accommodation.system.entity.NotificationSetting;
import com.accommodation.system.entity.Notifications;
import com.accommodation.system.entity.info.NotificationSettingInfo;
import com.accommodation.system.entity.model.NotificationMessage;
import com.accommodation.system.entity.request.PostRequest;

import java.io.IOException;

public interface NotificationService {
    void saveNotification(NotificationMessage notificationMessage);

    Notifications getNotifications(int userId);

    NotificationSettingInfo getNotificationSetting(int userId);

    boolean createNotificationSetting(NotificationSetting notificationSetting);

    boolean updateNotificationSetting(NotificationSetting notificationSetting);

    void pushNotificationsMatching(PostRequest postRequest, String postId) throws IOException;
}
