package com.accommodation.system.service.impl;

import com.accommodation.system.define.Constant;
import com.accommodation.system.entity.model.NotificationMessage;
import com.accommodation.system.entity.model.NotificationTokenInfo;
import com.accommodation.system.service.NotificationTokenService;
import com.accommodation.system.uitls.FirebaseUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;

/**
 * User: huongnq4
 * Date:  11/06/2020
 * Time: 16 :27
 * To change this template use File | Settings | File and Code Templates.
 */
@Service
public class NotificationTokenServiceImpl implements NotificationTokenService {
    @Override
    public void saveTokenFirebase(NotificationTokenInfo notificationTokenInfo) {
        //save db

        //check to subcibe
        NotificationMessage notificationMessage = NotificationMessage.builder()
                .to(String.format(Constant.FIREBASE_USER_TOPIC_PATTERN, notificationTokenInfo.getUserId()))
                .registrationTokens(Collections.singletonList(notificationTokenInfo.getDeviceToken()))
                .data(NotificationMessage.Data.builder()
                        .notificationInfo(NotificationMessage.NotificationInfo.builder()
                                .entityType(Constant.NotificationMessage.EntityType.SUBSCRIBE)
                                .build())
                        .build())
                .build();
        try {
            FirebaseUtil.send(notificationMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
