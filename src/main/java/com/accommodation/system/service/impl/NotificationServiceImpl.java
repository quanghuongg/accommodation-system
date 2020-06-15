package com.accommodation.system.service.impl;

import com.accommodation.system.entity.model.NotificationMessage;
import com.accommodation.system.service.NotificationService;
import com.accommodation.system.utils2.FirebaseUtil;
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
    @Override
    public void saveNotification(NotificationMessage notificationMessage)  {
        //mappper
    }

    @Override
    public void sendBatchNotifications(NotificationMessage notificationMessages, boolean isSend) throws IOException {
        FirebaseUtil.send(notificationMessages);
    }
}
