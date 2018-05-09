package com.enjoywater.service;

/**
 * Created by TONG HAI on 3/19/2018.
 */

public class ApiConstant {
    public static class Url {
        public static final String BASE_DOMAIN = "http://api.enjoywater.vn/";
        //CUSTOMER
        public static final String CHECK_EMAIL = "customer/check-email";
        public static final String CHECK_PHONE = "customer/check-tel";
        public static final String REGISTER = "customer/register";
        public static final String LOGIN = "customer/login";
        public static final String SEND_DIVICE_TOKEN = "customer/send-device-token";
        public static final String SAVE_PARENT = "customer/save-parent";
        public static final String EDIT_INFO = "customer/edit-info";
        //PRODUCT
        public static final String GET_TOP_SALE = "product/index";
        public static final String GET_NEAREST_SALE = "product/index";
        //NEWS
        public static final String GET_NEWS = "news/index";
        public static final String GET_ALL_NEWS = "news/get-all";
        public static final String GET_DETAIL_NEWS = "news/detail";
        //ORDER
        public static final String ORDER = "order/add";
        public static final String ORDER_HISTORY = "order/history";
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
        public static final String LATITUDE = "lat";
        public static final String LONGITUDE = "long";
        public static final String ADDRESS = "address";
    }

    public static class Value {
        public static final int STATUS_CODE_SUCCESS = 200;
        public static final int STATUS_CODE_ERROR = 401;
    }
}
