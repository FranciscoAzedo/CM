package com.example.christmasapp.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.example.christmasapp.data.model.PointOfInterest;
import com.example.christmasapp.ui.pois.PointsOfInterestFragment;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReadPointOfInterestImageTask extends AsyncTask<Void, Void, Void> {

    private List<PointOfInterest> pointOfInterestList = new ArrayList<>();
    PointsOfInterestFragment pointsOfInterestFragment;

    public ReadPointOfInterestImageTask(PointsOfInterestFragment pointsOfInterestFragment, List<PointOfInterest> pointOfInterestList) {
        this.pointsOfInterestFragment = pointsOfInterestFragment;
        this.pointOfInterestList = pointOfInterestList;
    }


    @Override
    protected void onPostExecute(Void arg) {
        pointsOfInterestFragment.updatePointOfInterestImages(pointOfInterestList);
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
        for (PointOfInterest pointOfInterest : pointOfInterestList) {
            URL url = new URL(pointOfInterest.getImageUrl());
            Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            pointOfInterest.setBitmap(bitmap);
        }
    }
}
