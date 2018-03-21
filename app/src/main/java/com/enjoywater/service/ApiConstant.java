package com.enjoywater.service;

/**
 * Created by TONG HAI on 3/19/2018.
 */

public class ApiConstant {
    public static class Url {
        public static final String BASE_DOMAIN = "http://api.enjoywater.vn/";
        // CUSTOMER
        public static final String CHECK_EMAIL = "/customer/check-email";
        public static final String CHECK_PHONE = "/customer/check-tel";
        public static final String REGISTER = "/customer/register";
        public static final String LOGIN = "/customer/login";
        public static final String SEND_DIVICE_TOKEN = "/customer/send-device-token";
        public static final String SAVE_PARENT = "/customer/save-parent";
        // PRODUCT
    }

    public static class Param {
        public static final String EMAIL = "email";
        public static final String PHONE = "tel";
        public static final String FULLNAME = "fullname";
        public static final String PASSWORD = "password";
        public static final String TOKEN = "token";
        public static final String DEVICE_TOKEN = "device_token";
        public static final String TYPE = "type";
        public static final String ID = "id";
    }

    public static class Value {
        public static final int STATUS_CODE_SUCCESS = 200;
        public static final int STATUS_CODE_ERROR = 401;
    }
}
