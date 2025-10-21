package com.example.se1707_prm392_g2_petshop.data.constants;

public class ConstantApi {
    // Auth API
    public static final String REGISTER = "Auth/register";
    public static final String LOGIN = "Auth/login";
    public static final String LOGIN_GOOGLE = "Auth/login-by-google";
    public static final String LOGIN_FACEBOOK= "Auth/login-by-facebook";
    public static final String LOGOUT = "Auth/logout";
    public static final String FORGOTPASSWORD_REQUEST = "Auth/forgot-password/request";
    public static final String FORGOTPASSWORD_VERIFY = "Auth/forgot-password/verify";
    public static final String RESET_PASSWORD = "Auth/forgot-password/reset";

    //Users
    public static final String GET_USER_BY_ID = "Users/{id}";

    // Chat
    public static final String CREATE_ROOM = "Chat/create-room";
    public static final String GET_ROOM_BY_CUSTOMER_ID = "Chat/{customerId}";
    public static final String SEND_MESSAGE = "Chat/send-message";
    public static final String GET_ROOMS = "Chat/messages/{chatRoomId}";

    // User Address
    public static final String GET_USER_ADDRESSES = "UserAddress/default/{userId}";
}
