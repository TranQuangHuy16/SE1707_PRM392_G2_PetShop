package com.example.se1707_prm392_g2_petshop.data.models;

import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("categoryId")
    public int categoryId;
    @SerializedName("categoryName")
    public String categoryName;
    @SerializedName("isActive")
    public boolean isActive;

    @SerializedName("imageUrl")
    public String imageUrl;



    public Category(int categoryId, String categoryName, boolean isActive, String imageUrl) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.isActive = isActive;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
