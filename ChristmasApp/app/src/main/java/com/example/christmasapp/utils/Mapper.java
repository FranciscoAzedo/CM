package com.example.christmasapp.utils;

import com.example.christmasapp.data.model.Event;
import com.example.christmasapp.data.model.PointOfInterest;
import com.example.christmasapp.data.model.dto.EventDTO;
import com.example.christmasapp.data.model.dto.PointOfInterestDTO;

import java.util.ArrayList;
import java.util.List;

public class Mapper {
    public static List<PointOfInterest> poiMapper(List<PointOfInterestDTO> pointsOfInterest) {
        List<PointOfInterest> pointOfInterestList = new ArrayList<>();

        for(PointOfInterestDTO pointOfInterest : pointsOfInterest) {
            pointOfInterestList.add(
                    new PointOfInterest(
                            pointOfInterest.getName(),
                            pointOfInterest.getImageUrl(),
                            pointOfInterest.getLocation(),
                            pointOfInterest.getBitmap(),
                            pointOfInterest.getDescription(),
                            false
                    )
            );
        }
        return pointOfInterestList;
    }

    public static List<Event> eventMapper(List<EventDTO> eventDTOList) {
        List<Event> eventList = new ArrayList<>();

        for(EventDTO event : eventDTOList) {
            eventList.add(
                    new Event(
                            event.getName(),
                            event.getImageUrl(),
                            event.getLocation(),
                            event.getBitmap(),
                            event.getDescription(),
                            false,
                            event.getSchedule(),
                            event.getPrice(),
                            event.getAgenda()
                    )
            );
        }
        return eventList;
    }
}
