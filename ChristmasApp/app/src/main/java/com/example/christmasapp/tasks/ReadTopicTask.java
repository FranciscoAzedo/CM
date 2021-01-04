package com.example.christmasapp.tasks;

import android.os.AsyncTask;
import android.os.Bundle;

import com.example.christmasapp.ChristmasActivity;
import com.example.christmasapp.helpers.SharedPreferencesHelper;
import com.example.christmasapp.utils.Constants;
import com.example.christmasapp.helpers.MqttHelper;
import com.example.christmasapp.ui.subscriptions.SubscriptionsFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class ReadTopicTask extends AsyncTask<Void, Void, Void> {

    private final SharedPreferencesHelper sharedPreferencesHelper;
    private final SubscriptionsFragment subscriptionsFragment;
    private final MqttHelper mqttHelper;

    private Set<String> topics;

    public ReadTopicTask(SubscriptionsFragment subscriptionsFragment, Bundle bundle) {
        this.sharedPreferencesHelper = SharedPreferencesHelper.getInstance(null);
        this.subscriptionsFragment = subscriptionsFragment;
        this.mqttHelper = MqttHelper.getInstance((ChristmasActivity) bundle.getSerializable(Constants.ACTIVITY_KEY));
    }

    @Override
    protected Void doInBackground(Void... args) {
        topics = sharedPreferencesHelper.getSharedPreference(Constants.SHARED_PREFERENCES_TOPIC_KEY);

        for (String topic : topics)
            mqttHelper.subscribeToTopic(topic);

        if (subscriptionsFragment != null) {
            Collections.reverse(new ArrayList<>(topics));
            subscriptionsFragment.updateSubscriptions(topics);
        }
        return null;
    }
}
