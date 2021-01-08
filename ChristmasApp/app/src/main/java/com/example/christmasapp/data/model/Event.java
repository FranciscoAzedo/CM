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
     * Represents the schedule of the event
     */
    private String schedule;
    /**
     * Represents the price to enter the event
     */
    private String price;
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
    public Event(String name, String imageUrl, Location location, Bitmap bitmap, String description, boolean isSubscribed, String schedule, String price, List<AgendaInstance> agenda) {
        super(name, imageUrl, location, bitmap, description, isSubscribed);
        this.schedule = schedule;
        this.price = price;
        this.agenda = agenda;
    }

    /**
     * Gets event's schedule
     *
     * @return event's schedule
     */
    public String getSchedule() {
        return schedule;
    }

    /**
     * Sets event's schedule
     *
     * @param schedule the schedule be assigned to the event
     */
    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    /**
     * Gets event's entry price
     *
     * @return event's entry price
     */
    public String getPrice() {
        return price;
    }

    /**
     * Sets event's entry price
     *
     * @param price the entry price to be assigned to the event
     */
    public void setPrice(String price) {
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
