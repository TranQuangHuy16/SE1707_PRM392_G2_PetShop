package com.example.se1707_prm392_g2_petshop.data.models;

import java.util.Date;

public class ProductRating {
    private int ratingId;
    private int userId;
    private int productId;
    private int stars;
    private String comment;
    private Date createdAt; 

    //  Constructor mặc định
    public ProductRating() {}

    //  Constructor đầy đủ
    public ProductRating(int ratingId, int userId, int productId, int stars, String comment, Date createdAt) {
        this.ratingId = ratingId;
        this.userId = userId;
        this.productId = productId;
        this.stars = stars;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    //  Getter & Setter
    public int getRatingId() {
        return ratingId;
    }

    public void setRatingId(int ratingId) {
        this.ratingId = ratingId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
