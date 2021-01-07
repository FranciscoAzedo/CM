package com.example.christmasapp.data.model;

/**
 * The class Location contains the required data to represent the geographical location of a
 * Java Object. It is used to represent geographically our POIs and Events.
 * <p>
 * The inherent methods allow the manipulation of the data.
 */
public class Location {
    /**
     * Represents the Location's name
     */
    private String name;
    /**
     * Represents the location's latitude
     */
    private double latitude;
    /**
     * Represents the location's longitude
     */
    private double longitude;

    /**
     * Constructor
     *
     * @param name the name to be assigned to the location
     * @param latitude  the latitude to be assigned to the location
     * @param longitude the longitude to be assigned to the location
     */
    public Location(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Get location's name
     *
     * @return location's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets location's name
     *
     * @param name the name to be assigned to the location
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets location's latitude
     *
     * @return location's latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets location's latitude
     *
     * @param latitude the latitude to be assigned to the location
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets location's longitude
     *
     * @return location's longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets location's longitude
     *
     * @param longitude the longitude to be assigned to the location
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Location{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
