package com.accommodation.system.utils2;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * User: nguyentrung
 * Date: 9/22/19
 * Time: 5:30 PM
 * To change this template use File | Settings | File and Code Templates.
 */
public class NotificationUtil {
    public static String genUUID(String content, String topic) {
        content = content + topic + UUID.randomUUID().toString() + System.currentTimeMillis();
        UUID uuid = UUID.nameUUIDFromBytes(content.getBytes(StandardCharsets.UTF_8));
        return uuid.toString();
    }
}
