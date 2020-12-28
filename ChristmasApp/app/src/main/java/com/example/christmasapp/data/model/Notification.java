package com.example.christmasapp.data.model;

import java.io.Serializable;

public class Notification implements Serializable {
    private int id;
    private String title;
    private String description;
    private Boolean read;

    public Notification(String title, String description, Boolean read) {
        this.title = title;
        this.description = description;
        this.read = read;
    }

    public Notification() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }
}
