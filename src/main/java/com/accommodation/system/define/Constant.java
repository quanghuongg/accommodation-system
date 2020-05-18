package com.accommodation.system.define;

import com.accommodation.system.uitls.ConfigurationLoader;

public class Constant {
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

    public static final class UserInfo {

        public static final String DEFAULT_AVATAR = "http://icons.iconarchive.com/icons/papirus-team/papirus-status/256/avatar-default-icon.png";

        public static final class JsonField {

            public static final String USER_ID = "user_id";
            public static final String USERNAME = "username";
            public static final String FACEBOOK_UID = "facebookUid";
            public static final String GOOGLE_UID = "googleUid";
            public static final String PASSWORD = "password";
            public static final String EMAIL = "email";
            public static final String CREATED_AT = "created_at";
            public static final String UPDATED_AT = "updated_at";
            public static final String FULL_NAME = "full_name";
            public static final String AVATAR = "avatar";
            public static final String ROLE_ID = "role_id";
            public static final String ID_TOKEN = "id_token";
        }
    }
}
