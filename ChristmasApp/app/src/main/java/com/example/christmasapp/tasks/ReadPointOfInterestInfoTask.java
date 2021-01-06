package com.example.christmasapp.tasks;

import android.os.AsyncTask;

import androidx.fragment.app.Fragment;

import com.example.christmasapp.data.model.Event;
import com.example.christmasapp.data.model.PointOfInterest;
import com.example.christmasapp.ui.map.MapFragment;
import com.example.christmasapp.ui.pois.PointsOfInterestFragment;
import com.example.christmasapp.utils.Constants;
import com.example.christmasapp.utils.JsonReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
        List<PointOfInterest> monumentsPointOfInterestList = new ArrayList<>();
        List<Event> eventsPointOfInterestList = new ArrayList<>();
        try {
            monumentsPointOfInterestList = JsonReader.readMonumentsJsonFromUrl(Constants.JSON_MONUMENTS_API_URL);
            eventsPointOfInterestList = JsonReader.readEventsJsonFromUrl(Constants.JSON_EVENTS_API_URL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            rawListToList(monumentsPointOfInterestList, eventsPointOfInterestList);
        }
    }

    private void rawListToList(List<PointOfInterest> monumentsPointOfInterestList, List<Event> eventsPointOfInterestList) {
        for (PointOfInterest pointOfInterest : monumentsPointOfInterestList) {
            pointOfInterestList.add( new PointOfInterest(
                    pointOfInterest.getName(),
                    pointOfInterest.getImageUrl(),
                    pointOfInterest.getLocation(),
                    pointOfInterest.getBitmap()
                )
            );
        }

        for (Event event : eventsPointOfInterestList) {
            pointOfInterestList.add( new Event(
                    event.getName(),
                    event.getImageUrl(),
                    event.getLocation(),
                    event.getBitmap(),
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
