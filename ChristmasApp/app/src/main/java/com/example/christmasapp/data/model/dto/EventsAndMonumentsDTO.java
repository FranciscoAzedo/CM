package com.example.christmasapp.data.model.dto;


import java.io.Serializable;
import java.util.List;

public class EventsAndMonumentsDTO implements Serializable {
    List<EventDTO> events;
    List<PointOfInterestDTO> pointsOfInterest;

    public EventsAndMonumentsDTO(List<EventDTO> events, List<PointOfInterestDTO> pointsOfInterest) {
        this.events = events;
        this.pointsOfInterest = pointsOfInterest;
    }

    public List<EventDTO> getEvents() {
        return events;
    }

    public List<PointOfInterestDTO> getPointsOfInterest() {
        return pointsOfInterest;
    }
}
