package com.example.se1707_prm392_g2_petshop.data.api;

import com.example.se1707_prm392_g2_petshop.data.dtos.requests.ProductRatingRequest;
import com.example.se1707_prm392_g2_petshop.data.models.ProductRating;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProductRatingApi {

    // Lấy danh sách đánh giá của sản phẩm
    @GET("products/{productId}/ratings")
    Call<List<ProductRating>> getRatings(@Path("productId") int productId);

    //  Gửi đánh giá mới
    @POST("products/{productId}/ratings")
    Call<Void> addRating(@Path("productId") int productId, @Body ProductRatingRequest request);
}
