package com.example.christmasapp.data.model;


public class PointOfInterest {

    private String name;
    private Type type;
    private Location location;

    enum Type {
        //TODO Add PoI types
    }

    class Location {
        private double latitude;
        private double longitude;
    }
}
