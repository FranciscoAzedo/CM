package com.example.christmasapp.data.model;

import java.io.Serializable;

public class NotificationDTO implements Serializable {
    private String title;
    private String description;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
