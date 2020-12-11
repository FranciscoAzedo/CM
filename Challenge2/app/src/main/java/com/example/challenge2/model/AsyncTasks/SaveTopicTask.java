package com.example.challenge2.model.AsyncTasks;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.challenge2.R;
import com.example.challenge2.Utils;
import com.example.challenge2.model.Repository.NoteKeeperDBHelper;
import com.example.challenge2.model.Topic;
import com.example.challenge2.view.NoteActivity;
import com.example.challenge2.view.NotificationManager;
import com.google.android.material.snackbar.Snackbar;

public class SaveTopicTask extends AsyncTask<Void, Void, Void> {

    private final FragmentActivity activity;
    private final Bundle bundle;
    private final NotificationManager notificationManager;
    private final NoteKeeperDBHelper noteKeeperDBHelper;
    private final Topic topic;
    private boolean result;

    public SaveTopicTask(FragmentActivity activity, Bundle bundle) {
        this.activity = activity;
        this.bundle = bundle;
        this.notificationManager = (NoteActivity) bundle.getSerializable(Utils.ACTIVITY_KEY);
        this.noteKeeperDBHelper = (NoteKeeperDBHelper) bundle.getSerializable(Utils.DATABASE_HELPER_KEY);
        this.topic = (Topic) bundle.getSerializable(Utils.TOPIC_KEY);
    }

    @Override
    protected void onPostExecute(Void arg) {
        int message;
        if (result) {
            message = R.string.create_topic_success;
            notificationManager.notifyNewTopic(bundle);
        } else {
            message = R.string.create_topic_error;
        }

        Snackbar.make(activity.getWindow().getDecorView().findViewById(R.id.RelativeLayout),
                message,
                Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Void doInBackground(Void... args) {
        int id = noteKeeperDBHelper.addTopic(topic);
        if (id > 0) {
            topic.setId(id);
            bundle.putSerializable(Utils.TOPIC_KEY, topic);
            result = true;
        } else {
            result = false;
        }
        return null;
    }
}