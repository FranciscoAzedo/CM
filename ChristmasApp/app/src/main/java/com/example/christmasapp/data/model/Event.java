package com.example.christmasapp.data.model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.sql.Time;
import java.util.List;

/**
 * The class Event inherit Points of Interest since its a specialization that contains more required
 * logic and data. It is a class used to represent all the required information of an event.
 * <p>
 * The inherent methods allow the manipulation of the data.
 */
public class Event extends PointOfInterest implements Serializable {

    /**
     * Represents the opening time of the event
     */
    private Time openTime;
    /**
     * Represents the closing time of the event
     */
    private Time closeTime;
    /**
     * Represents the price to enter the event
     */
    private double price;
    /**
     * Represents the agenda of this event
     */
    private List<AgendaInstance> agenda;

    /**
     * Constructor
     *
     * @param name     the name to be assigned to the POI
     * @param imageUrl the image URL to be assigned to the POI
     * @param location the location to be assigned to the POI
     * @param bitmap   the image bitmap to be assigned to the POI
     */
    public Event(String name, String imageUrl, Location location, Bitmap bitmap, String description, boolean isSubscribed, Time openTime, Time closeTime, double price, List<AgendaInstance> agenda) {
        super(name, imageUrl, location, bitmap, description, isSubscribed);
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.price = price;
        this.agenda = agenda;
    }

    /**
     * Gets event's opening time
     *
     * @return event's opening time
     */
    public Time getOpenTime() {
        return openTime;
    }

    /**
     * Sets event's opening time
     *
     * @param openTime the opening time to be assigned to the event
     */
    public void setOpenTime(Time openTime) {
        this.openTime = openTime;
    }

    /**
     * Gets event's closing time
     *
     * @return event's closing time
     */
    public Time getCloseTime() {
        return closeTime;
    }

    /**
     * Sets event's closing time
     *
     * @param closeTime the closing time to be assigned to the event
     */
    public void setCloseTime(Time closeTime) {
        this.closeTime = closeTime;
    }

    /**
     * Gets event's entry price
     *
     * @return event's entry price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets event's entry price
     *
     * @param price the entry price to be assigned to the event
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets event's agenda
     *
     * @return event's agenda
     */
    public List<AgendaInstance> getAgenda() {
        return agenda;
    }

    /**
     * Sets event's agenda
     *
     * @param agenda the agenda to be assigned to the event
     */
    public void setAgenda(List<AgendaInstance> agenda) {
        this.agenda = agenda;
    }
}
