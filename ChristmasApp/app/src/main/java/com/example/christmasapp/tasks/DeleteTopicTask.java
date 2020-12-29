package com.example.christmasapp.tasks;

import android.os.AsyncTask;
import android.os.Bundle;

import com.example.christmasapp.ChristmasActivity;
import com.example.christmasapp.NotificationManager;
import com.example.christmasapp.data.Utils;
import com.example.christmasapp.data.model.Topic;
import com.example.christmasapp.helpers.MqttHelper;

public class DeleteTopicTask extends AsyncTask<Void, Void, Void> {

    private final Bundle bundle;
    private final NotificationManager notificationManager;
    private final MqttHelper mqttHelper;
    private boolean result;

    public DeleteTopicTask(Bundle bundle) {
        this.bundle = bundle;
        this.notificationManager = (ChristmasActivity) bundle.getSerializable(Utils.ACTIVITY_KEY);
        this.mqttHelper = MqttHelper.getInstance((ChristmasActivity) bundle.getSerializable(Utils.ACTIVITY_KEY));
    }

    @Override
    protected void onPostExecute(Void arg) {
        if (result) {
            Topic topic = (Topic) bundle.getSerializable(Utils.TOPIC_KEY);
            mqttHelper.unSubscribeToTopic(topic.getName());
            notificationManager.notifyDeletedTopic(bundle);
        }
    }

    @Override
    protected Void doInBackground(Void... args) {
        result = Utils.updateTopics(Utils.DELETE_TOPIC_MODE, bundle);
        return null;
    }
}
