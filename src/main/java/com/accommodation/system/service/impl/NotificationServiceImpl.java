package com.accommodation.system.service.impl;

import com.accommodation.system.dao.PostDao;
import com.accommodation.system.entity.*;
import com.accommodation.system.entity.model.NotificationMessage;
import com.accommodation.system.entity.model.SearchResult;
import com.accommodation.system.entity.request.PostRequest;
import com.accommodation.system.mapper.*;
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

    @Autowired
    UserMapper userMapper;

    @Autowired
    PostDao postDao;

    @Autowired
    NotificationsMapper notificationsMapper;

    @Override
    public void saveNotification(Notifications notifications) {
        notificationsMapper.addNotification(notifications);
    }

    @Override
    public void updateReadAt(int notificationId, long readAt) {
        notificationsMapper.updateReadAt(notificationId, readAt);
    }

    @Override
    public SearchResult listNotification(int userId) {
        SearchResult searchResult = new SearchResult();
        searchResult.setCount(notificationsMapper.countNotificationUnread(userId).size());
        searchResult.setHits(notificationsMapper.listNotificationByUserId(userId));
        return searchResult;
    }

    @Override
    public NotificationSetting getNotificationSetting(int userId) {
        NotificationSetting setting = notificationSettingMapper.findByUser(userId);
        return setting;
    }

    @Async("threadPoolTaskExecutor")
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
            if (update.getMaxArea() > 0) {
                record.setMaxArea(update.getMaxArea());
                record.setMinArea(update.getMinArea());
            }
            if (update.getDistrictId() > 0) {
                record.setDistrictId(update.getDistrictId());
            }
            if (update.getWardId() > 0) {
                record.setWardId(update.getWardId());
            }
            if (update.getMaxPrice() > 0) {
                record.setMaxPrice(update.getMaxPrice());
                record.setMinPrice(update.getMinPrice());
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
                        .body(" ✍️: " + postRequest.getDescription() + "...")
                        .color("green")
                        .priority("high")
                        .title("Có thông tin phòng trọ phù hợp với bạn!")
                        .build()).build();
        for (Integer id : userIds) {
            if (id != userId) {
                notificationMessage.setTo("/topics/Test");
                FirebaseUtil.send(notificationMessage);
                //Save notification MySQL
                notificationsMapper.addNotification(Notifications.builder()
                        .userId(id)
                        .createdAt(System.currentTimeMillis())
                        .message("Có thông tin phòng trọ phù hợp với bạn!")
                        .postId(postId)
                        .build());
            }
        }
    }

    List<Integer> listUserMatching(PostRequest postRequest) {
        List<Integer> userIds = new ArrayList<>();
        List<NotificationSetting> list = notificationSettingMapper.findNotificationSetting();
        for (NotificationSetting notificationSetting : list) {
            if (isMatching(postRequest, notificationSetting)) {
                userIds.add(notificationSetting.getUserId());
            }
        }
        return userIds;
    }

    boolean isMatching(PostRequest postRequest, NotificationSetting setting) {
        if (setting.getDistrictId() > 0) {
            if (postRequest.getDistrictId() != setting.getDistrictId()) {
                return false;
            }
        }
        if (setting.getWardId() > 0) {
            if (postRequest.getWardId() != setting.getWardId()) {
                return false;
            }
        }
        if (setting.getMaxPrice() > 0) {
            if (postRequest.getPrice() < setting.getMinPrice() || postRequest.getPrice() > setting.getMaxPrice()) {
                return false;
            }
        }
        if (setting.getMaxArea() > 0) {
            if (postRequest.getArea() < setting.getMinArea() || postRequest.getArea() > setting.getMaxArea()) {
                return false;
            }
        }

        if (setting.getRoomTypeId() > 0) {
            return postRequest.getRoomTypeId() == setting.getRoomTypeId();
        }

        return true;
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public void pushNotificationComment(Comment comment) throws IOException {
        Post post = postDao.find(comment.getPostId());
        if (Utils.isNotEmpty(post)) {
            int ownerPost = post.getUserId();
            User user = userMapper.findByUserId(comment.getUserId());
            String postUser = "Có người";
            if (Utils.isNotEmpty(user)) {
                postUser = user.getDisplayName();
            }
            String content = comment.getContent();
            if (content.length() > 50) {
                content.substring(0, 50);
            }
            //Send firebase
            NotificationMessage notificationMessage = NotificationMessage.builder()
                    .to("/topics/Test")
                    .userId(comment.getUserId())
                    .data(NotificationMessage.Data.builder()
                            .postId(comment.getPostId())
                            .build())
                    .notification(NotificationMessage.Notification.builder()
                            .body(" ✍️: " + content + "...")
                            .color("green")
                            .priority("high")
                            .title(postUser + " bình luận về bài đăng của bạn.")
                            .build()).build();
            FirebaseUtil.send(notificationMessage);
            //Save notification MySQL
            notificationsMapper.addNotification(Notifications.builder()
                    .userId(ownerPost)
                    .createdAt(System.currentTimeMillis())
                    .message(postUser + " bình luận về bài đăng của bạn.")
                    .postId(comment.getPostId())
                    .build());
        }
    }
}
