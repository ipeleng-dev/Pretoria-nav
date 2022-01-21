package com.poe.preotrianav.models;

public class bookmarkLandmark {
    private String title;
    private String photoRef;

    public bookmarkLandmark() {
    }

    public bookmarkLandmark(String title, String photoRef) {
        this.title = title;
        this.photoRef = photoRef;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhotoRef() {
        return photoRef;
    }

    public void setPhotoRef(String photoRef) {
        this.photoRef = photoRef;
    }
}
