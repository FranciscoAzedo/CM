package com.example.christmasapp.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.christmasapp.ChristmasActivity;
import com.example.christmasapp.data.model.Topic;
import com.example.christmasapp.helpers.SharedPreferencesHelper;
import com.example.christmasapp.utils.Constants;
import com.example.christmasapp.helpers.MqttHelper;
import com.example.christmasapp.ui.subscriptions.SubscriptionsFragment;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

public class ReadTopicTask extends AsyncTask<Void, Void, Void> {

    private final SharedPreferencesHelper sharedPreferencesHelper;
    private final SubscriptionsFragment subscriptionsFragment;
    private final MqttHelper mqttHelper;

    private Set<Topic> topics;

    public ReadTopicTask(SubscriptionsFragment subscriptionsFragment, Bundle bundle) {
        this.sharedPreferencesHelper = SharedPreferencesHelper.getInstance(null);
        this.subscriptionsFragment = subscriptionsFragment;
        this.mqttHelper = MqttHelper.getInstance((ChristmasActivity) bundle.getSerializable(Constants.ACTIVITY_KEY));
    }

    @Override
    protected Void doInBackground(Void... args) {
        topics = sharedPreferencesHelper.getSharedPreference(Constants.SHARED_PREFERENCES_TOPIC_KEY);

        downloadImages();

        for (Topic topic : topics)
            mqttHelper.subscribeToTopic(topic.getName());

        if (subscriptionsFragment != null) {
            subscriptionsFragment.updateSubscriptions(topics);
        }
        return null;
    }

    private void downloadImages() {
        try {
            for (Topic topic : topics) {
                URL url = new URL(topic.getImageUrl());
                Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                topic.setBitmap(bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
