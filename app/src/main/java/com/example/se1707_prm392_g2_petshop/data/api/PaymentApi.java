package com.example.se1707_prm392_g2_petshop.data.api;

import com.example.petshop.dto.ZaloPayCreateRequest;
import com.example.petshop.dto.ZaloPayCreateResponse;
import com.example.petshop.dto.ZaloPayQueryResponse;
import com.example.se1707_prm392_g2_petshop.data.constants.ConstantApi;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.CreatePaymentRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.ConfirmPaymentResponse;
import com.example.se1707_prm392_g2_petshop.data.models.Payment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PaymentApi {
    @GET(ConstantApi.GET_MY_PAYMENTS)
    Call<List<Payment>> getMyPayments(@Query("status") Integer status);

    @GET(ConstantApi.GET_PAYMENT_BY_ID)
    Call<Payment> getPaymentById(@Path("id") int id);

    @GET(ConstantApi.GET_PAYMENT_BY_ORDER_ID)
    Call<Payment> getPaymentByOrderId(@Path("orderId") int orderId);

    @POST(ConstantApi.CREATE_PAYMENT)
    Call<Payment> createPayment(@Body CreatePaymentRequest request);

    @POST(ConstantApi.ZALOPAY_CREATE)
    Call<ZaloPayCreateResponse> createZaloPayPayment(@Body ZaloPayCreateRequest request);

    @POST(ConstantApi.ZALOPAY_QUERY)
    Call<ZaloPayQueryResponse> queryZaloPayPayment(@Path("orderId") int orderId);

    @POST(ConstantApi.CONFIRM_PAYMENT)
    Call<ConfirmPaymentResponse> confirmPayment(@Path("orderId") int orderId);
}
