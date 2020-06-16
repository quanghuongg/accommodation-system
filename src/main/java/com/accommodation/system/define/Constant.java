package com.accommodation.system.define;

import com.accommodation.system.uitls.ConfigurationLoader;

public class Constant {

    public static final String FIREBASE_SEND_MESSAGE_ADDRESS = ConfigurationLoader.getInstance().getAsString("firebase.send.message.address", "https://fcm.googleapis.com/fcm/send");
    public static final String FIREBASE_LEGACY_SERVER_KEY = ConfigurationLoader.getInstance().getAsString("firebase.legacy.server.key", "AIzaSyBcI9ITmB4AyXOfbZFDKasLDp0ULeJjkVo");
    public static final String FIREBASE_TOPIC_SUBSCRIBE_ADDRESS = ConfigurationLoader.getInstance().getAsString("firebase.topic.subscribe.address", "https://iid.googleapis.com/iid/v1:batchAdd");
    public static final String FIREBASE_TOPIC_UNSUBSCRIBE_ADDRESS = ConfigurationLoader.getInstance().getAsString("firebase.topic.unsubscribe.address", "https://iid.googleapis.com/iid/v1:batchRemove");
    public static final String FIREBASE_USER_TOPIC_PATTERN = ConfigurationLoader.getInstance().getAsString("firebase_user_topic_pattern", "/topics/orm.notification.user.%s");

    public static final boolean IGNORE_UNAVAILABLE = true;
    public static final boolean ALLOW_NO_INDICES = false;
    public static final boolean EXPAND_TO_OPEN_INDICES = true;
    public static final boolean EXPAND_TO_CLOSED_INDICES = true;


    public static final String HOST_STATIC_WEB  ="http://huongnq.s3-website-ap-southeast-1.amazonaws.com/";
    public static final String BUCKET_NAME ="huongnq";

    public static final String SDF_FORMAT = "yyyy/MM/dd HH:mm:ss";
    public static final int FAILED_CODE = 1;
    public static final String LOGIN_FAIL = "login fail";
    public static final int SUCCESS_CODE = 0;
    public static final String SUCCESS_MESSAGE = "success";
    public static final String OBJECT_EMPTY_FIELD = "object empty";
    public static final String USER_CREATE_EXISTING = "user name created";
    public static final String USER_NOT_EXITED = "user not exited";

    public static final class MailConfiguration {
        public static final String MAIL_USERNAME = ConfigurationLoader.getInstance().getAsString("mail.username", "quanghuongitus@gmail.com");
        public static final String MAIL_PASSWORD = ConfigurationLoader.getInstance().getAsString("mail.password", "hmzmmvfxsjtcpzde");
        public static final String MAIL_HOST = ConfigurationLoader.getInstance().getAsString("mail.host", "125.235.240.36");
        public static final int MAIL_PORT = ConfigurationLoader.getInstance().getAsInteger("mail.port", 465);

        public static final int MAIL_SMTP_SOCKET_FACTORY_PORT = ConfigurationLoader.getInstance().getAsInteger("mail.smtp.socketFactory.port", 465);
        public static final boolean MAIL_SMTP_AUTH = ConfigurationLoader.getInstance().getAsBoolean("mail.smtp.auth", true);
        public static final boolean MAIL_SMTP_STARTTLS_ENABLE = ConfigurationLoader.getInstance().getAsBoolean("mail.smtp.starttls.enable", true);
        public static final String MAIL_SMTP_SSL_TRUST = ConfigurationLoader.getInstance().getAsString("mail.smtp.ssl.trust", "*");
        public static final String MAIL_SMTP_SOCKET_FACTORY_CLASS = ConfigurationLoader.getInstance().getAsString("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        public static final boolean MAIL_SMTP_SOCKETFACTORY_FALLBACK = ConfigurationLoader.getInstance().getAsBoolean("mail.smtp.socketFactory.fallback", false);
        public static final String MAIL_STORE_PROTOCOL = ConfigurationLoader.getInstance().getAsString("mail.store.protocol", "pop3");
        public static final String MAIL_TRANSPORT_PROTOCOL = ConfigurationLoader.getInstance().getAsString("mail.transport.protocol", "smtp");
    }

    public static final class SecurityConstant {
        public static final String JWT_SECRET = "nZr4u7x!A%D*G-KaPdSgUkXp2s5v8y/B?E(H+MbQeThWmYq3t6w9z$C&F)J@NcRf";
        public static final String TOKEN_HEADER = "Authorization";
        public static final String TOKEN_PREFIX = "Bearer ";
        public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 864000000;
        public static final String AUTHORITIES_KEY = "auth";
    }

    public static final class SocialAccountInfo {
        public static class JsonField {
            public static final String ID_TOKEN = "id_token";
            public static final String TYPE = "type";
            public static final String ROLE_ID = "role_id";
        }
    }
    public static final class FileUploader {
        public static final String PATH_AVATARS = ConfigurationLoader.getInstance().getAsString("path.avatars", "avatars");
        public static final String PATH_IMAGES = ConfigurationLoader.getInstance().getAsString("path.avatars", "images");


        public static final class MediaType {
            public static final String IMAGE_EXTENSION = ".jpg";
            public static final int IMAGE_MAX_WIDTH = 2048;
        }
    }

    public static final class NotificationMessage {
        public static final class EntityType {
            public static final String SUBSCRIBE = "/subscribe";
            public static final String UNSUBSCRIBE = "/unsubscribe";
        }
        public static final class ReadType {
            public static final int READ = 1;
            public static final int UNREAD = 0;
            public static final int ALL = 2;
        }

    }


    public static final class Order {
        public static final class Type {
            public static final int CREATE_TIME_ASC = 0;
            public static final int CREATE_TIME_DESC = 1;
            public static final int PRICE_ASC = 2;
            public static final int PRICE_DESC = 3;
            public static final int AREA_ASC = 4;
            public static final int AREA_DESC = 5;
        }
    }

    public static class Post {
        public static class JsonField {
            public static final String DISTRICT_ID = "district_id";
            public static final String WARD_ID = "ward_id";
            public static final String AREA = "area";
            public static final String ROOM_TYPE_ID = "room_type_id";
            public static final String LOCATION = "location";
            public static final String CREATED_AT = "created_at";
            public static final String PRICE = "price";
        }
    }
}
