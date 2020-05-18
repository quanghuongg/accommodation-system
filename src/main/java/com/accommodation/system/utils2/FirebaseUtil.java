package com.accommodation.system.utils2;

import viettel.vtcc.common.constant.Constant;
import viettel.vtcc.common.model.NotificationMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FirebaseUtil {

    public static void send(NotificationMessage notificationMessage) throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "key=" + Constant.FIREBASE_LEGACY_SERVER_KEY);

        String entityType = notificationMessage.getData().getNotificationInfo().getEntityType();

        String url = Constant.FIREBASE_SEND_MESSAGE_ADDRESS;
        switch (entityType) {
            case Constant.NotificationMessage.EntityType.SUBSCRIBE:
                url = Constant.FIREBASE_TOPIC_SUBSCRIBE_ADDRESS;
                break;
            case Constant.NotificationMessage.EntityType.UNSUBSCRIBE:
                url = Constant.FIREBASE_TOPIC_UNSUBSCRIBE_ADDRESS;
                break;
        }

        RestUtil.doPost(url, notificationMessage, headers);
    }
}
