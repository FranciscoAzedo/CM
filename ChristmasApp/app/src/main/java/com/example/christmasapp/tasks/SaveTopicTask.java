package com.example.christmasapp.tasks;

import android.os.AsyncTask;
import android.os.Bundle;

import com.example.christmasapp.ChristmasActivity;
import com.example.christmasapp.NotificationManager;
import com.example.christmasapp.data.model.Topic;
import com.example.christmasapp.utils.Constants;
import com.example.christmasapp.utils.Utils;
import com.example.christmasapp.helpers.MqttHelper;

public class SaveTopicTask extends AsyncTask<Void, Void, Void> {


    private final Bundle bundle;
    private final NotificationManager notificationManager;
    private final MqttHelper mqttHelper;
    private boolean result;

    public SaveTopicTask(Bundle bundle) {
        this.bundle = bundle;
        this.notificationManager = (ChristmasActivity) bundle.getSerializable(Constants.ACTIVITY_KEY);
        this.mqttHelper = MqttHelper.getInstance((ChristmasActivity) bundle.getSerializable(Constants.ACTIVITY_KEY));
    }

    @Override
    protected void onPostExecute(Void arg) {
        Boolean subscribed = bundle.getBoolean(Constants.MQTT_SUBSCRIBED);
        Topic topic = (Topic) bundle.getSerializable(Constants.TOPIC_KEY);
        if (subscribed) {
            if (result) {
                notificationManager.notifyNewTopic(bundle);
            } else {
                mqttHelper.unSubscribeToTopic(topic.getName());
            }
        }
    }

    @Override
    protected Void doInBackground(Void... args) {
        Boolean subscribed = bundle.getBoolean(Constants.MQTT_SUBSCRIBED);
        if (subscribed)
            result = Utils.updateTopics(Constants.CREATE_TOPIC_MODE, bundle);
        else{
            Topic topic = (Topic) bundle.getSerializable(Constants.TOPIC_KEY);
            mqttHelper.subscribeToTopic(topic, true);
        }
        return null;
    }
}