package com.example.se1707_prm392_g2_petshop.data.dtos.responses;

import com.example.se1707_prm392_g2_petshop.data.models.ProductRating;

import java.util.List;

public class ProductRatingResponse {
    private double averageStars;
    private int count;
    private List<ProductRating> ratings;

    public double getAverageStars() {
        return averageStars;
    }

    public void setAverageStars(double averageStars) {
        this.averageStars = averageStars;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ProductRating> getRatings() {
        return ratings;
    }

    public void setRatings(List<ProductRating> ratings) {
        this.ratings = ratings;
    }
}