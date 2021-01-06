package com.example.christmasapp.data.model.dto;

import java.io.Serializable;

/**
 * The class NotificationDTO contains the required data to represent a notification data received
 * from an MQTT publisher about an event.
 * <p>
 * The inherent methods allow the manipulation of the data.
 */
public class NotificationDTO implements Serializable {
    /**
     * Represents the notification's title
     */
    private String title;
    /**
     * Represents the notification's description
     */
    private String description;

    /**
     * Gets notification's title
     *
     * @return notification's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets notification's description
     *
     * @return notification's description
     */
    public String getDescription() {
        return description;
    }
}