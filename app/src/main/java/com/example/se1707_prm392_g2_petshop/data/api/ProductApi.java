package com.example.se1707_prm392_g2_petshop.data.api;

import com.example.se1707_prm392_g2_petshop.data.constants.ConstantApi;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.CreateProductRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.UpdateProductRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.ProductRatingRequest;
import com.example.se1707_prm392_g2_petshop.data.models.Product;
import com.example.se1707_prm392_g2_petshop.data.models.ProductRating;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductApi {
    @GET(ConstantApi.GET_ALL_PRODUCTS)
    Call<List<Product>> getAllProducts();

    @GET(ConstantApi.GET_ALL_PRODUCTS_NOT_ACTIVE)
    Call<List<Product>> getAllProductsNotActive();

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

    @GET(ConstantApi.GET_PRODUCTS_BY_CATEGORY_ID_NOT_ACTIVE)
    Call<List<Product>> getProductsByCategoryIdNotActive(@Path("categoryId") int categoryId);

    // Search sản phẩm
    @GET(ConstantApi.SEARCH_PRODUCTS)
    Call<List<Product>> searchProducts(
            @Query("keyword") String keyword,
            @Query("category") String category,
            @Query("brand") String brand,
            @Query("minPrice") Double minPrice,
            @Query("maxPrice") Double maxPrice
    );

    // Đánh giá sản phẩm
    @GET(ConstantApi.GET_PRODUCT_RATINGS)
    Call<List<ProductRating>> getProductRatings(@Path("productId") int productId);

    @POST("api/products/{productId}/ratings")
    Call<Void> addProductRating(
            @Path("productId") int productId,
            @Body ProductRatingRequest request
    );
}
