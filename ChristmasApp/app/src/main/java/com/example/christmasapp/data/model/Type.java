package com.example.christmasapp.data.model;

import java.io.Serializable;

/**
 * The enumeration Type represents the types of POIs that the system handles
 */
public enum Type implements Serializable {
    EVENT,
    MONUMENT;

    @Override
    public String toString() {
        return this.name();
    }
}