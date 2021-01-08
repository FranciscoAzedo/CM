package com.example.christmasapp.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import androidx.fragment.app.Fragment;

import com.example.christmasapp.data.model.PointOfInterest;
import com.example.christmasapp.ui.map.MapFragment;
import com.example.christmasapp.ui.pois.Event_Detailed.EventDetailedFragment;
import com.example.christmasapp.ui.pois.Monument_Detailed.MonumentDetailedFragment;
import com.example.christmasapp.ui.pois.PointsOfInterestFragment;

import java.io.IOException;
import java.net.URL;

public class ReadPointOfInterestImageTask extends AsyncTask<Void, Void, Void> {

    private PointOfInterest pointOfInterest;
    Fragment fragment;

    public ReadPointOfInterestImageTask(Fragment fragment, PointOfInterest pointOfInterest) {
        this.fragment = fragment;
        this.pointOfInterest = pointOfInterest;
    }


    @Override
    protected void onPostExecute(Void arg) {
        if(fragment instanceof PointsOfInterestFragment)
            ((PointsOfInterestFragment) fragment).updatePointOfInterestImages();
        else if (fragment instanceof MonumentDetailedFragment)
            ((MonumentDetailedFragment) fragment).updatePointOfInterestImages(pointOfInterest);
        else if (fragment instanceof EventDetailedFragment)
            ((EventDetailedFragment) fragment).updatePointOfInterestImages(pointOfInterest);
    }

    @Override
    protected Void doInBackground(Void... args) {
        try {
            downloadImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void downloadImage() throws IOException {
        URL url = new URL(pointOfInterest.getImageUrl());
        Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        pointOfInterest.setBitmap(bitmap);
    }
}
