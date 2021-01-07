package com.example.christmasapp.tasks;

import android.os.AsyncTask;

import androidx.fragment.app.Fragment;

import com.example.christmasapp.data.model.Event;
import com.example.christmasapp.data.model.PointOfInterest;
import com.example.christmasapp.data.model.dto.EventsAndMonumentsDTO;
import com.example.christmasapp.ui.map.MapFragment;
import com.example.christmasapp.ui.pois.PointsOfInterestFragment;
import com.example.christmasapp.utils.Constants;
import com.example.christmasapp.utils.JsonReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReadPointOfInterestInfoTask extends AsyncTask<Void, Void, Void> {

    private List<PointOfInterest> pointOfInterestList = new ArrayList<>();
    Fragment fragment;

    public ReadPointOfInterestInfoTask(Fragment pointsOfInterestFragment) {
        this.fragment = pointsOfInterestFragment;
    }

    @Override
    protected void onPostExecute(Void arg) {
        if(fragment instanceof PointsOfInterestFragment)
            ((PointsOfInterestFragment) fragment).updatePointOfInterestInfo(pointOfInterestList);
        else if(fragment instanceof MapFragment)
            ((MapFragment) fragment).fetchPOIs(pointOfInterestList);
    }

    @Override
    protected Void doInBackground(Void... args) {
        fetchJson();
        return null;
    }

    private void fetchJson() {
        EventsAndMonumentsDTO eventsAndMonumentsDTO = null;
        try {
            eventsAndMonumentsDTO = JsonReader.readMonumentsJsonFromUrl(Constants.JSON_POI_API_URL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            rawListToList(eventsAndMonumentsDTO);
        }
    }

    private void rawListToList(EventsAndMonumentsDTO eventsAndMonumentsDTO) {
        for (PointOfInterest pointOfInterest : eventsAndMonumentsDTO.getPointsOfInterest()) {
            pointOfInterestList.add( new PointOfInterest(
                    pointOfInterest.getName(),
                    pointOfInterest.getImageUrl(),
                    pointOfInterest.getLocation(),
                    pointOfInterest.getBitmap(),
                    pointOfInterest.getDescription()
                )
            );
        }

        for (Event event : eventsAndMonumentsDTO.getEvents()) {
            pointOfInterestList.add( new Event(
                    event.getName(),
                    event.getImageUrl(),
                    event.getLocation(),
                    event.getBitmap(),
                    event.getDescription(),
                    event.getOpenTime(),
                    event.getCloseTime(),
                    event.getPrice(),
                    event.getAgenda()
                )
            );
        }

        Collections.shuffle(pointOfInterestList);
    }
}
