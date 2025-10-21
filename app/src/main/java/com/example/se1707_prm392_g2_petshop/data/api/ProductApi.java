package com.example.se1707_prm392_g2_petshop.data.api;

import com.example.se1707_prm392_g2_petshop.data.constants.Constant;
import com.example.se1707_prm392_g2_petshop.data.constants.ConstantApi;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.CreateProductRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.UpdateProductRequest;
import com.example.se1707_prm392_g2_petshop.data.models.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProductApi {
    @GET(ConstantApi.GET_ALL_PRODUCTS)
    Call<List<Product>> getAllProducts();

    @GET(ConstantApi.GET_PRODUCT_BY_ID)
    Call<Product> getProductById(@Path("id") int id);

    @POST(ConstantApi.CREATE_PRODUCT)
    Call<Product> createProduct(@Body CreateProductRequest request);

    @PUT(ConstantApi.UPDATE_PRODUCT)
    Call<Boolean> updateProduct(@Path("id") int id, @Body UpdateProductRequest request);

    @DELETE(ConstantApi.DELETE_PRODUCT)
    Call<Void> deleteProduct(@Path("id") int id);

    @GET(ConstantApi.GET_PRODUCTS_BY_CATEGORY_ID)
    Call<List<Product>> getProductsByCategoryId(@Path("categoryId") int categoryId);

}
