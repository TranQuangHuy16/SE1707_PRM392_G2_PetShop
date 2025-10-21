package com.example.se1707_prm392_g2_petshop.data.dtos.responses;

public class CategoryResponse {
    public int categoryId;
    public String categoryName;
    public boolean isActive;
    public String imageUrl;

    public CategoryResponse(int categoryId, String categoryName, boolean isActive,String imageUrl) {
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
