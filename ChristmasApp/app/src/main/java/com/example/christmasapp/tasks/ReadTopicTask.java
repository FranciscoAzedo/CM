package com.example.christmasapp.tasks;

import android.os.AsyncTask;
import android.os.Bundle;

import com.example.christmasapp.ChristmasActivity;
import com.example.christmasapp.data.Utils;
import com.example.christmasapp.data.model.Topic;
import com.example.christmasapp.helpers.DatabaseHelper;
import com.example.christmasapp.helpers.MqttHelper;
import com.example.christmasapp.ui.subscriptions.SubscriptionsFragment;

import java.util.Collections;
import java.util.List;

public class ReadTopicTask extends AsyncTask<Void, Void, Void> {

    private final DatabaseHelper databaseHelper;
    private final SubscriptionsFragment subscriptionsFragment;
    private final MqttHelper mqttHelper;

    private List<Topic> topics;

    public ReadTopicTask(SubscriptionsFragment subscriptionsFragment, Bundle bundle) {
        this.databaseHelper = DatabaseHelper.getInstance(null);
        this.subscriptionsFragment = subscriptionsFragment;
        this.mqttHelper = MqttHelper.getInstance((ChristmasActivity) bundle.getSerializable(Utils.ACTIVITY_KEY));
    }

    @Override
    protected Void doInBackground(Void... args) {
        topics = databaseHelper.getAllTopics();

        for (Topic topic : topics)
            mqttHelper.subscribeToTopic(topic.getName());

        if (subscriptionsFragment != null) {
            Collections.reverse(topics);
            subscriptionsFragment.updateSubscriptions(topics);
        }
        return null;
    }
}
