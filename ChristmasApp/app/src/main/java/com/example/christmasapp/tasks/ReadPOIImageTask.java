package com.example.christmasapp.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import androidx.fragment.app.Fragment;

import com.example.christmasapp.data.model.PointOfInterest;
import com.example.christmasapp.ui.map.MapFragment;
import com.example.christmasapp.ui.pois.PointsOfInterestFragment;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReadPOIImageTask extends AsyncTask<Void, Void, Bitmap> {

    private Fragment fragment;
    private String url;
    private int index;

    public ReadPOIImageTask(Fragment fragment, String url, int index) {
        this.fragment = fragment;
        this.url = url;
        this.index = index;
    }

    @Override
    protected void onPostExecute(Bitmap arg) {
        if(fragment instanceof MapFragment)
            ((MapFragment) fragment).fetchImageOnPOI(index, arg);
    }

    @Override
    protected Bitmap doInBackground(Void... args) {
        try {
            return downloadImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap downloadImage() throws IOException {
        URL url = new URL(this.url);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        return BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, options);
    }
}
