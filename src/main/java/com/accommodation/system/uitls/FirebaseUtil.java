package com.accommodation.system.uitls;


import com.accommodation.system.define.Constant;
import com.accommodation.system.entity.model.NotificationMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FirebaseUtil {
    public static void send(NotificationMessage notificationMessage) throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "key=" + Constant.FIREBASE_LEGACY_SERVER_KEY);
        RestUtil.doPost(Constant.FIREBASE_SEND_MESSAGE_ADDRESS, notificationMessage, headers);
    }
}
