package com.example.se1707_prm392_g2_petshop.data.dtos.requests;

public class ProductRatingRequest {
    private int userId;
    private int stars;
    private String comment;

    public ProductRatingRequest(int userId, int stars, String comment) {
        this.userId = userId;
        this.stars = stars;
        this.comment = comment;
    }

    // Getter và Setter
    public int getUserId() { return userId; }
    public int getStars() { return stars; }
    public String getComment() { return comment; }
}
