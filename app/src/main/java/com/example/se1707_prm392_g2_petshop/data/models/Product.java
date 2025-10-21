package com.example.se1707_prm392_g2_petshop.data.models;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("productId")
    public int productId;
    @SerializedName("categoryId")
    public int categoryId;
    @SerializedName("productName")
    public String productName;
    @SerializedName("description")
    public String description ;
    @SerializedName("price")
    public double price;
    @SerializedName("stock")
    public int stock;
    @SerializedName("imageUrl")
    public String imageUrl;
    @SerializedName("isActive")
    public boolean isActive;

    @SerializedName("categoryName")
    public String categoryName;

    public Product(int productId, int categoryId,String categoryName, String productName, String description, double price, int stock, String imageUrl, boolean isActive) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.isActive = isActive;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public double getPrice() {
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
