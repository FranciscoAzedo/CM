package com.example.challenge2.model.AsyncTasks;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.challenge2.MqttHelper;
import com.example.challenge2.R;
import com.example.challenge2.Utils;
import com.example.challenge2.model.Note;
import com.google.android.material.snackbar.Snackbar;

public class PublishNoteTask extends AsyncTask<Void, Void, Void> {

    private final FragmentActivity activity;
    private final Bundle bundle;
    private final MqttHelper mqttHelper;
    private boolean result;

    public PublishNoteTask(FragmentActivity activity, Bundle bundle) {
        this.activity = activity;
        this.bundle = bundle;
        this.mqttHelper = (MqttHelper) bundle.getSerializable(Utils.MQTT_HELPER_KEY);
    }

    @Override
    protected void onPostExecute(Void arg) {
        int message;
        if (result)
            message = R.string.published_note_success;
        else
            message = R.string.published_note_error;

        Snackbar.make(activity.getWindow().getDecorView().findViewById(R.id.RelativeLayout),
                message,
                Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Void doInBackground(Void... args) {
        Note note = (Note) bundle.getSerializable(Utils.NOTE_KEY);
        if (mqttHelper != null && note != null) {
            mqttHelper.publishToTopic(Utils.PUBLISH_TOPIC_KEY, Utils.serializeNote(note).getBytes());
            result = true;
        } else {
            result = false;
        }
        return null;
    }
}