package com.example.christmasapp.data.model.dto;

import android.graphics.Bitmap;

import com.example.christmasapp.data.model.Location;

import java.io.Serializable;

/**
 * The class PointOfInterest contains the required data to represent a given point of interest. A
 * point of interest is considered, on the system, a monument or some place, or an event that is
 * involved in the christmas spirit.
 * <p>
 * The inherent methods allow the manipulation of the data.
 */
public class PointOfInterestDTO implements Serializable {
    /**
     * Represents the POI's name
     */
    private String name;
    /**
     * Represents the POI's location
     */
    private Location location;
    /**
     * Represent's the POI's image bitmap
     */
    private Bitmap bitmap;
    /**
     * Represents the POI's image URL
     */
    private String imageUrl;
    /**
     * Represents the POI's description
     */
    private String description;
    /**
     * Constructor
     *
     * @param name     the name to be assigned to the POI
     * @param imageUrl the image URL to be assigned to the POI
     * @param location the location to be assigned to the POI
     * @param bitmap   the image bitmap to be assigned to the POI
     * @param description the description to be assigned to the POI
     */
    public PointOfInterestDTO(String name, String imageUrl, Location location, Bitmap bitmap, String description) {
        this.name = name;
        this.location = location;
        this.bitmap = bitmap;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    /**
     * Gets POI's name
     *
     * @return POI's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets POI's name
     *
     * @param name the name to be assigned to the POI
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets POI's image URL
     *
     * @return POI's image URL
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets POI's image URL
     *
     * @param imageUrl the image URL to be assigned to the POI
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Gets POI's location
     *
     * @return POI's location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets POI's location
     *
     * @param location the location to be assigned to the POI
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Gets POI's image bitmap
     *
     * @return POI's image bitmap
     */
    public Bitmap getBitmap() {
        return bitmap;
    }

    /**
     * Sets POI's image bitmap
     *
     * @param bitmap the image bitmap to be assigned to the POI
     */
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    /**
     * Gets POI's description
     *
     * @return POI's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets POI's description
     *
     * @param description the name to be assigned to the POI
     */
    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public String toString() {
        return "PointOfInterest{" +
                "name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
//                ", type=" + type +
                ", location=" + location +
                ", imageView=" + bitmap +
                '}';
    }
}
