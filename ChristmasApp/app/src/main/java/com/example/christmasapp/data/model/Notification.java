package com.example.christmasapp.data.model;

import java.io.Serializable;
import java.util.Date;

/**
 * The class Notification contains the required data to represent a notification about some event.
 * Is used as a warning for the user know about some action on an event that him subscribes.
 * <p>
 * The inherent methods allow the manipulation of the data.
 */
public class Notification implements Serializable {
    /**
     * Notification's identifier
     */
    private Integer id;
    /**
     * Represents the notification's title
     */
    private String title;
    /**
     * Represents the notification's description
     */
    private String description;
    /**
     * Flag to shows if the notifications has been read
     */
    private Boolean read;
    /**
     * Represents the notification's timestamp
     */
    private Date date;

    /**
     * Default Constructor
     */
    public Notification() {
    }

    /**
     * Constructor
     *
     * @param title       the title to be assigned to the notification
     * @param description the description to be assigned to the notification
     * @param read        the 'read' flag status to be assigned to the notification
     */
    public Notification(String title, String description, Boolean read) {
        this.title = title;
        this.description = description;
        this.read = read;
    }

    /**
     * Gets notification's identifier
     *
     * @return notification's identifier
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets notification's identifier
     *
     * @param id the identifier to be assigned to the notification
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets notification's title
     *
     * @return notification's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets notification's title
     *
     * @param title the title to be assigned to the notification
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets notification's description
     *
     * @return notification's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets notification's title
     *
     * @param description the description to be assigned to the notification
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets notification's read flag
     *
     * @return the notification's read flag status
     */
    public Boolean getRead() {
        return read;
    }

    /**
     * Sets notification's read flag
     *
     * @param read the notification's read flag status to be assigned to the notification
     */
    public void setRead(Boolean read) {
        this.read = read;
    }

    /**
     * Gets notification's timestamp
     *
     * @return the notification's timestamp
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets notification's timestamp
     *
     * @param date the notification's timestamp to be assigned to the notification
     */
    public void setDate(Date date) {
        this.date = date;
    }
}
