package com.accommodation.system.entity.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class NotificationMessage implements Serializable {
    private static final long serialVersionUID = -8480498069748902050L;

    private int userId;

    private String to;

    private String createdBy;

    @JsonProperty(value = "content_available")
    @Builder.Default
    boolean contentAvailable = true;

    @JsonProperty(value = "registration_tokens")
    private List<String> registrationTokens;

    private Notification notification;

    private Data data;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @Builder
    public static class Notification implements Serializable {
        private static final long serialVersionUID = 8199940414118526451L;

        private String body;

        private String title;

        private String subtitle;

        private String color;

        private String priority;

        private String group;

        @Builder.Default
        private String sound = "default";

        @JsonProperty("android_channel_id")
        @Builder.Default
        private String androidChannelId="default";

        private String id;

        @JsonProperty("show_in_foreground")
        @Builder.Default
        private boolean showInForeground = true;

        private int vibrate;

        private String postId;

    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @Builder
    public static class Data implements Serializable {
        private static final long serialVersionUID = -1889469923634042793L;

        @JsonProperty("notification_info")
        private NotificationInfo notificationInfo;

        @JsonProperty("click_action")
        @Builder.Default
        private String clickAction = "FLUTTER_NOTIFICATION_CLICK";
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @Builder
    public static class NotificationInfo implements Serializable {
        private static final long serialVersionUID = -3428679414098243648L;

        @JsonProperty("index_name")
        private List<String> indexNames;

        private List<String> ids;

        @JsonProperty("entity_type")
        private String entityType;

        private String content;

        private String status;

        @JsonProperty("notification_id")
        private String notificationId;

    }
}
