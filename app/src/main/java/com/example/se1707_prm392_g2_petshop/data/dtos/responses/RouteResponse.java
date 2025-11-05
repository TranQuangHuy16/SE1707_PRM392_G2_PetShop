package com.example.se1707_prm392_g2_petshop.data.dtos.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RouteResponse {
    @SerializedName("coordinates")
    private List<List<Double>> coordinates;

    @SerializedName("distance")
    private Double distance;

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    @SerializedName("duration")
    private Double duration;

    public List<List<Double>> getCoordinates() {
        return coordinates;
    }
}

