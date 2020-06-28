package com.accommodation.system.service.impl;

import com.accommodation.system.entity.*;
import com.accommodation.system.entity.info.NotificationSettingInfo;
import com.accommodation.system.entity.model.NotificationMessage;
import com.accommodation.system.entity.request.PostRequest;
import com.accommodation.system.mapper.DistrictMapper;
import com.accommodation.system.mapper.NotificationSettingMapper;
import com.accommodation.system.mapper.RoomTypeMapper;
import com.accommodation.system.mapper.WardMapper;
import com.accommodation.system.service.NotificationService;
import com.accommodation.system.uitls.FirebaseUtil;
import com.accommodation.system.uitls.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    WardMapper wardMapper;

    @Autowired
    DistrictMapper districtMapper;

    @Autowired
    RoomTypeMapper roomTypeMapper;

    @Override
    public void saveNotification(NotificationMessage notificationMessage) {
    }

    @Override
    public Notifications getNotifications(int userId) {
        return null;
    }

    @Override
    public NotificationSettingInfo getNotificationSetting(int userId) {
        NotificationSetting setting = notificationSettingMapper.findByUser(userId);
        if (setting == null) {
            return null;
        }
        NotificationSettingInfo notificationSettingInfo = NotificationSettingInfo.builder()
                .id(setting.getId())
                .area(setting.getArea())
                .location(setting.getLocation())
                .price(setting.getPrice())
                .enable(setting.getEnable())
                .build();
        Ward ward = wardMapper.findWard(setting.getWardId());
        if (ward != null) {
            notificationSettingInfo.setWard(ward.getName());
        }
        District district = districtMapper.findDistrict(setting.getDistrictId());
        if (district != null) {
            notificationSettingInfo.setDistrict(district.getName());
        }
        RoomType roomType = roomTypeMapper.find(setting.getRoomTypeId());
        if (roomType != null) {
            notificationSettingInfo.setRoomType(roomType.getName());
        }
        return notificationSettingInfo;
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
            if (update.getEnable() != null) {
                record.setEnable(update.getEnable());
            }
            notificationSettingMapper.updateNotificationSetting(record);
        }
        return true;
    }

    @Async("threadPoolTaskExecutor")
    @Override
    public void pushNotificationsMatching(PostRequest postRequest, String postId, int userId) throws IOException {
        List<Integer> userIds = listUserMatching(postRequest);
        NotificationMessage notificationMessage = NotificationMessage.builder()
                .userId(userId)
                .data(NotificationMessage.Data.builder()
                        .postId(postId)
                        .build())
                .notification(NotificationMessage.Notification.builder()
                        .body("Mô tả: " + postRequest.getDescription())
                        .color("green")
                        .priority("high")
                        .title("Có thông tin phòng trọ phù hợp với bạn!")
                        .build()).build();
        for (Integer id : userIds) {
            if (id != userId) {
                notificationMessage.setTo("/topics/Test");
                FirebaseUtil.send(notificationMessage);
            }
        }
    }

    List<Integer> listUserMatching(PostRequest postRequest) {
        List<Integer> userIds = new ArrayList<>();
        List<NotificationSetting> list = notificationSettingMapper.findNotificationSetting(postRequest.getDistrictId());
        for (NotificationSetting notificationSetting : list) {
            if (isMatching(postRequest, notificationSetting)) {
                userIds.add(notificationSetting.getUserId());
            }
        }
        return userIds;
    }

    boolean isMatching(PostRequest postRequest, NotificationSetting setting) {
        if (setting.getWardId() > 0) {
            if (postRequest.getWardId() != setting.getWardId()) {
                return false;
            }
        }
        if (setting.getPrice() > 0) {
            if (postRequest.getPrice() < setting.getPrice() * 0.8 || postRequest.getPrice() > setting.getPrice() * 1.2) {
                return false;
            }
        }
        if (setting.getArea() > 0) {
            if (postRequest.getArea() < setting.getArea() * 0.8 || postRequest.getArea() > setting.getArea() * 1.2) {
                return false;
            }
        }

        if (setting.getRoomTypeId() > 0) {
            if (postRequest.getRoomTypeId() != setting.getRoomTypeId()) {
                return false;
            }
        }

        if (setting.getRoomTypeId() > 0) {
            return postRequest.getRoomTypeId() == setting.getRoomTypeId();
        }

        return true;
    }
}
