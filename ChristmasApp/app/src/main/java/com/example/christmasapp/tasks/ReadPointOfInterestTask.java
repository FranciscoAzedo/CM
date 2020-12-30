package com.example.christmasapp.tasks;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.christmasapp.ChristmasActivity;
import com.example.christmasapp.NotificationManager;
import com.example.christmasapp.data.model.PointOfInterest;
import com.example.christmasapp.utils.Constants;
import com.example.christmasapp.utils.JsonReader;
import com.example.christmasapp.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadPointOfInterestTask extends AsyncTask<Void, Void, Void> {

    private List<PointOfInterest> pointOfInterestList = new ArrayList<>();

    public ReadPointOfInterestTask() {
    }

    @Override
    protected void onPostExecute(Void arg) {
        for(PointOfInterest pointOfInterest : pointOfInterestList) {
            Log.d("poi", pointOfInterest.toString());
        }
    }

    @Override
    protected Void doInBackground(Void... args) {
        try {
            pointOfInterestList = JsonReader.readJsonFromUrl(Constants.JSON_API_URL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
