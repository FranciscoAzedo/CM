package com.example.christmasapp.data.model.dto;

import com.example.christmasapp.data.model.Event;
import com.example.christmasapp.data.model.PointOfInterest;

import java.io.Serializable;
import java.util.List;

public class EventsAndMonumentsDTO implements Serializable {
    List<Event> events;
    List<PointOfInterest> pointsOfInterest;

    public EventsAndMonumentsDTO(List<Event> events, List<PointOfInterest> pointsOfInterest) {
        this.events = events;
        this.pointsOfInterest = pointsOfInterest;
    }

    public List<Event> getEvents() {
        return events;
    }

    public List<PointOfInterest> getPointsOfInterest() {
        return pointsOfInterest;
    }
}
