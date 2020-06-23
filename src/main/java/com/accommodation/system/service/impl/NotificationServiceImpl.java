package com.accommodation.system.service.impl;

import com.accommodation.system.entity.NotificationSetting;
import com.accommodation.system.entity.Notifications;
import com.accommodation.system.entity.model.NotificationMessage;
import com.accommodation.system.entity.request.PostRequest;
import com.accommodation.system.mapper.NotificationSettingMapper;
import com.accommodation.system.service.NotificationService;
import com.accommodation.system.uitls.FirebaseUtil;
import com.accommodation.system.uitls.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * User: huongnq4
 * Date:  11/06/2020
 * Time: 16 :36
 * To change this template use File | Settings | File and Code Templates.
 */

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    NotificationSettingMapper notificationSettingMapper;

    @Override
    public void saveNotification(NotificationMessage notificationMessage) {
    }

    @Override
    public Notifications getNotifications(int userId) {
        return null;
    }

    @Override
    public NotificationSetting getNotificationSetting(int userId) {
        return notificationSettingMapper.findByUser(userId);
    }

    @Override
    public boolean createNotificationSetting(NotificationSetting notificationSetting) {
        if (Utils.isNotEmpty(notificationSettingMapper.findByUser(notificationSetting.getUserId()))) {
            return true;
        }
        notificationSettingMapper.createNotificationSetting(notificationSetting);
        return true;
    }

    @Override
    public boolean updateNotificationSetting(NotificationSetting update) {
        NotificationSetting record = notificationSettingMapper.findByUser(update.getUserId());
        if (record != null) {
            if (update.getArea() > 0) {
                record.setArea(update.getArea());
            }
            if (update.getDistrictId() > 0) {
                record.setDistrictId(update.getDistrictId());
            }
            if (update.getWardId() > 0) {
                record.setWardId(update.getWardId());
            }
            if (update.getPrice() > 0) {
                record.setPrice(update.getPrice());
            }
            if (update.getRoomTypeId() > 0) {
                record.setRoomTypeId(update.getRoomTypeId());
            }
            if (update.getLocation() != null) {
                record.setLocation(update.getLocation());
            }
            notificationSettingMapper.updateNotificationSetting(record);
        }
        return true;
    }

    @Async("threadPoolTaskExecutor")
    @Override
    public void pushNotificationsMatching(PostRequest postRequest, String postId) throws IOException {
        NotificationMessage notificationMessage = NotificationMessage.builder()
                .to("/topics/Test").userId(12)
                .data(NotificationMessage.Data.builder()
                        .postId(postId)
                        .build())
                .notification(NotificationMessage.Notification.builder()
                        .body("Địa chỉ " + postRequest.getLocation())
                        .color("red")
                        .priority("green")
                        .title("Có thông tin phòng trọ phù hợp với bạn!")
                        .build()).build();
        FirebaseUtil.send(notificationMessage);
    }
}
