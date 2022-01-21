package com.poe.preotrianav.models;

public class landmark {
    private String title;
    private double lat;
    private double lng;
    private String photoRef;

    public landmark() {
    }

    public landmark(String title, double lat, double lng, String photoRef) {
        this.title = title;
        this.lat = lat;
        this.lng = lng;
        this.photoRef = photoRef;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getPhotoRef() {
        return photoRef;
    }

    public void setPhotoRef(String photoRef) {
        this.photoRef = photoRef;
    }
}
