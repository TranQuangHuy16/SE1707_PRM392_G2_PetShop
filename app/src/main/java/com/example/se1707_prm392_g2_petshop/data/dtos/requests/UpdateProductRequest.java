package com.example.se1707_prm392_g2_petshop.data.dtos.requests;

public class UpdateProductRequest {
    public int categoryId;
    public String productName;
    public String description ;
    public int price;
    public int stock;
    public String imageUrl;
    public boolean isActive;

    public UpdateProductRequest(int categoryId, String productName, String description, int price, int stock, String imageUrl, boolean isActive) {
        this.categoryId = categoryId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.isActive = isActive;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
