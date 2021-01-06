package com.example.christmasapp.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import androidx.fragment.app.Fragment;

import com.example.christmasapp.data.model.PointOfInterest;
import com.example.christmasapp.ui.map.MapFragment;
import com.example.christmasapp.ui.pois.PointsOfInterestFragment;
import com.example.christmasapp.utils.Constants;
import com.example.christmasapp.utils.JsonReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReadPointOfInterestTask extends AsyncTask<Void, Void, Void> {

    private List<PointOfInterest> pointOfInterestList = new ArrayList<>();
    Fragment fragment;

    public ReadPointOfInterestTask(Fragment pointsOfInterestFragment) {
        this.fragment = pointsOfInterestFragment;
    }

    @Override
    protected void onPostExecute(Void arg) {
        if(fragment instanceof PointsOfInterestFragment)
            ((PointsOfInterestFragment) fragment).updatePointOfInterest(pointOfInterestList);
        else if(fragment instanceof MapFragment)
            ((MapFragment) fragment).fetchPOIs(pointOfInterestList);
    }

    @Override
    protected Void doInBackground(Void... args) {
        fetchJson();

        if(fragment instanceof PointsOfInterestFragment) {
            try {
                downloadImage();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private void downloadImage() throws IOException {
        for (PointOfInterest pointOfInterest : pointOfInterestList) {
            URL url = new URL(pointOfInterest.getImageUrl());
            Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            pointOfInterest.setBitmap(bitmap);
        }
    }

    private void fetchJson() {
        try {
            pointOfInterestList = JsonReader.readJsonFromUrl(Constants.JSON_API_URL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
