package com.accommodation.system.define;

public class ContextPath {

    public static final class Home {
        public static final String HOME = "/home";
        public static final String DISTRICTS = "/districts";
        public static final String WARDS = "/wards";
        public static final String ROOM_TYPES = "/room-types";
    }

    public static class User {
        public static final String USER = "/user";
        public static final String INFO_GET = "/get-info";
        public static final String GET_ALL = "get-all";
        public static final String USER_RESET_PASSWORD = "/reset-password";
        public static final String REGISTER = "/register";
        public static final String CONFIRM = "/confirm";
        public static final String UPLOAD_AVATAR = "/upload-avatar";
        public static final String UPDATE_USER_INFO = "/update";
        public static final String LOGOUT = "/logout";
        public static final String ADD_USER_PIN = "/add-user-pin";
        public static final String UN_USER_PIN = "/un-user-pin";
        public static final String LIST_USER_PIN = "/list-user-pin";
        public static final String LIST_POST = "/list-post";
        public static final String VIEW_MY_POST = "/my-post";
        public static final String FEED_BACK = "/feedback";


    }

    public static class Notification {
        public static final String NOTIFICATION = "/notification";
        public static final String GET = "/get";
        public static final String UPDATE_READ_AT = "/update-read-at";
        public static class Setting {
            public static final String SETTING = "/setting";
            public static final String UPDATE = "/update";
            public static final String GET = "/get";
            public static final String CREATE = "/create";
        }
    }
    public static class Post {
        public static final String POST = "/post";
        public static final String CREATE = "/create";
        public static final String UPLOAD_IMAGES = "/upload-images";
        public static final String SEARCH = "/search";
        public static final String VIEW_DETAIL = "/view-detail";
        public static final String DELETE = "/delete";
        public static final String UPDATE = "/update";
        public static final String UPDATE_STATUS= "/update/status";


        public static class Comment {
            public static final String ADD = "/comment/add";
            public static final String LIST = "/comment/list";


        }
    }
}
