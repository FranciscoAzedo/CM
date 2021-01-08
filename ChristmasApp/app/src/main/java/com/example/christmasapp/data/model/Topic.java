package com.example.christmasapp.data.model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * The class Topic contains the required data to represent a topic that the user can subscribe in
 * order to obtain notifications.
 * <p>
 * The inherent methods allow the manipulation of the data.
 */
public class Topic implements Serializable {
    /**
     * Represents the topic's name
     */
    private String name;
    /**
     * Represents the topic's image URL
     */
    private String imageUrl;
    /**
     * Represents the topic's image bitmap
     */
    private Bitmap bitmap;

    private Long timestamp;

    /**
     * Default Constructor
     */
    public Topic() {
    }

    /**
     * Constructor
     *
     * @param name     the name to be assigned to the topic
     * @param imageUrl the image URL to be assigned to the topic
     */
    public Topic(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.bitmap = null;
        timestamp = System.currentTimeMillis();
    }

    /**
     * Gets topic's name
     *
     * @return topic's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets topic's name
     *
     * @param name the name to be assigned to the topic
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets topic's image URL
     *
     * @return topic's image URL
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets topic's image URL
     *
     * @param imageUrl the image URL to be assigned to the topic
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Gets topic's image bitmap
     *
     * @return topic's image bitmap
     */
    public Bitmap getBitmap() {
        return bitmap;
    }

    /**
     * Sets topic's image bitmap
     *
     * @param bitmap the image bitmap to be assigned to the topic
     */
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
