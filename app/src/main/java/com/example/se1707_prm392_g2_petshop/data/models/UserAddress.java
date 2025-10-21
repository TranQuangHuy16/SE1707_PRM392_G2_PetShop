package com.example.se1707_prm392_g2_petshop.data.models;

public class UserAddress {
    private int addressId;
    private int userId;
    private String addressLine;
    private String city;
    private String district;
    private String ward;
    private String postalCode;
    private boolean isDefault;
    private double latitude;
    private double longitude;

    public UserAddress(int addressId, int userId,
                       String addressLine, String city, String district, String ward, String postalCode,
                       boolean isDefault, double latitude, double longitude) {
        this.addressId = addressId;
        this.userId = userId;
        this.addressLine = addressLine;
        this.city = city;
        this.district = district;
        this.ward = ward;
        this.postalCode = postalCode;
        this.isDefault = isDefault;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}

