package com.example.se1707_prm392_g2_petshop.data.dtos.requests;

public class UpdateCategoryRequest {

    public String categoryName;
    public boolean isActive;
    public String imageUrl;

    public UpdateCategoryRequest(String categoryName, boolean isActive, String imageUrl) {
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
