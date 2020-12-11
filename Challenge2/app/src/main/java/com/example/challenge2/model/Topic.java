package com.example.challenge2.model;

import java.io.Serializable;

public class Topic implements Serializable {
    private int id;
    private String name;

    public Topic() {
    }

    public Topic(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
