package com.example.christmasapp.data.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Topic implements Serializable {
    private String name;
    private String imageUrl;
    private Bitmap bitmap;

    public Topic() {
    }

    public Topic(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.bitmap = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
