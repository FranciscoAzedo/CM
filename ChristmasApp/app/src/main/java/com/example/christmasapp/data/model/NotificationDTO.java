package com.example.christmasapp.data.model;

import java.io.Serializable;

/**
 *
 */
public class NotificationDTO implements Serializable {
    /**
     *
     */
    private String title;
    /**
     *
     */
    private String description;

    /**
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return
     */
    public String getDescription() {
        return description;
    }
}