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
    public static final String GET_ALL_USER_ADDRESSES = "UserAddress";

    //Category
    public static final String GET_ALL_CATEGORIES = "Categories";
    public static final String GET_CATEGORY_BY_ID = "Categories/{id}";
    public static final String CREATE_CATEGORY = "Categories";
    public static final String UPDATE_CATEGORY = "Categories/{id}";
    public static final String DELETE_CATEGORY = "Categories/{id}";

    //Product
    public static final String GET_ALL_PRODUCTS = "Products";
    public static final String GET_PRODUCT_BY_ID = "Products/{id}";
    public static final String CREATE_PRODUCT = "Products";
    public static final String UPDATE_PRODUCT = "Products/{id}";
    public static final String DELETE_PRODUCT = "Products/{id}";
    public static final String GET_PRODUCTS_BY_CATEGORY_ID = "Products/category/{categoryId}";

    // Cart
    public static final String GET_MY_CART = "Carts/my-cart";
    public static final String ADD_TO_CART = "Carts/add";
    public static final String UPDATE_CART_ITEM = "Carts/items/{cartItemId}";
    public static final String REMOVE_CART_ITEM = "Carts/items/{cartItemId}";
    public static final String CLEAR_CART = "Carts/clear";

    // Payment
    public static final String GET_MY_PAYMENTS = "Payments/my-payments";
    public static final String GET_PAYMENT_BY_ID = "Payments/{id}";
    public static final String GET_PAYMENT_BY_ORDER_ID = "Payments/order/{orderId}";
    public static final String CREATE_PAYMENT = "Payments";

    // ZaloPay
    public static final String ZALOPAY_CREATE = "ZaloPay/create";
    public static final String ZALOPAY_CALLBACK = "ZaloPay/callback";
    public static final String ZALOPAY_QUERY = "ZaloPay/query/{orderId}";
    public static final String CONFIRM_PAYMENT = "Payments/confirm/{orderId}";

    // Order
    public static final String CREATE_ORDER_FROM_CART = "Orders/create-from-cart";
    public static final String GET_MY_ORDERS = "Orders";
    public static final String GET_ORDER_BY_ID = "Orders/{orderId}";
    public static final String CANCEL_ORDER = "Orders/{orderId}/cancel";
}
