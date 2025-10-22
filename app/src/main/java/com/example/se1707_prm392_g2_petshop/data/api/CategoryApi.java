package com.example.se1707_prm392_g2_petshop.data.api;

import com.example.se1707_prm392_g2_petshop.data.constants.ConstantApi;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.CreateCategoryRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.UpdateCategoryRequest;
import com.example.se1707_prm392_g2_petshop.data.models.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CategoryApi {
    @GET(ConstantApi.GET_ALL_CATEGORIES)
    Call<List<Category>> getAllCategories();

    @GET(ConstantApi.GET_CATEGORY_BY_ID)
    Call<Category> getCategoryById(@Path("id") int id);

    @POST(ConstantApi.CREATE_CATEGORY)
    Call<Category> createCategory(@Body CreateCategoryRequest request);

    @PUT(ConstantApi.UPDATE_CATEGORY)
    Call<Boolean> updateCategory(@Path("id") int id, @Body UpdateCategoryRequest request);

    @DELETE(ConstantApi.DELETE_CATEGORY)
    Call<Void> deleteCategory(@Path("id") int id);
}
