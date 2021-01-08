package com.example.christmasapp.tasks;

import android.os.AsyncTask;

import androidx.fragment.app.Fragment;

import com.example.christmasapp.data.model.Event;
import com.example.christmasapp.data.model.PointOfInterest;
import com.example.christmasapp.data.model.Topic;
import com.example.christmasapp.data.model.dto.EventsAndMonumentsDTO;
import com.example.christmasapp.helpers.SharedPreferencesHelper;
import com.example.christmasapp.ui.map.MapFragment;
import com.example.christmasapp.ui.pois.PointsOfInterestFragment;
import com.example.christmasapp.ui.subscriptions.SubscriptionsFragment;
import com.example.christmasapp.utils.Constants;
import com.example.christmasapp.utils.JsonReader;
import com.example.christmasapp.utils.Mapper;
import com.example.christmasapp.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReadPointOfInterestInfoTask extends AsyncTask<Void, Void, Void> {

    private List<PointOfInterest> pointOfInterestList = new ArrayList<>();
    private List<Topic> topicList = new ArrayList<>();
    private SharedPreferencesHelper sharedPreferencesHelper;
    private Fragment fragment;

    public ReadPointOfInterestInfoTask(Fragment fragment) {
        this.fragment = fragment;
        this.sharedPreferencesHelper = SharedPreferencesHelper.getInstance(this.fragment.getActivity());
    }

    @Override
    protected void onPostExecute(Void arg) {
        this.topicList = sharedPreferencesHelper.getSharedPreference(Constants.SHARED_PREFERENCES_TOPIC_KEY);
        for (PointOfInterest pointOfInterest : pointOfInterestList)
            if (Utils.sharedPreferencesContainsPointOfInterest(pointOfInterest.getName(), topicList))
                pointOfInterest.setSubscribed(true);

        if(fragment instanceof PointsOfInterestFragment)
            ((PointsOfInterestFragment) fragment).updatePointOfInterestInfo(pointOfInterestList);
        else if(fragment instanceof MapFragment)
            ((MapFragment) fragment).fetchPOIsOnMap(pointOfInterestList);
        else if(fragment instanceof SubscriptionsFragment)
            ((SubscriptionsFragment) fragment).updatePointsOfInterest(pointOfInterestList);
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
        List<PointOfInterest> pointOfInterestList = Mapper.poiMapper(eventsAndMonumentsDTO.getPointsOfInterest());
        List<Event> eventList = Mapper.eventMapper(eventsAndMonumentsDTO.getEvents());

        for (PointOfInterest pointOfInterest : pointOfInterestList) {
            this.pointOfInterestList.add( new PointOfInterest(
                    pointOfInterest.getName(),
                    pointOfInterest.getImageUrl(),
                    pointOfInterest.getLocation(),
                    pointOfInterest.getBitmap(),
                    pointOfInterest.getDescription(),
                    false)
            );
        }

        for (Event event : eventList) {
            this.pointOfInterestList.add( new Event(
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

        Collections.shuffle(this.pointOfInterestList);
    }
}
