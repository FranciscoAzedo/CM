package com.example.challenge2.model.AsyncTasks;

import android.os.AsyncTask;
import android.os.Bundle;

import com.example.challenge2.Utils;
import com.example.challenge2.model.Repository.NoteKeeperDBHelper;
import com.example.challenge2.model.Topic;
import com.example.challenge2.view.NoteActivity;
import com.example.challenge2.view.NotificationManager;

import java.io.Serializable;
import java.util.List;


public class ReadTopicsTask extends AsyncTask<Void, Void, Void> {

    private final NoteKeeperDBHelper noteKeeperDBHelper;
    private final NotificationManager notificationManager;
    private final Bundle bundle;

    private List<Topic> topics;

    // Construtor da async task
    public ReadTopicsTask(Bundle bundle) {
        this.noteKeeperDBHelper = (NoteKeeperDBHelper) bundle.getSerializable(Utils.DATABASE_HELPER_KEY);
        this.notificationManager = (NoteActivity) bundle.getSerializable(Utils.ACTIVITY_KEY);
        this.bundle = bundle;
    }

    // Método executado quando a async task é chamada
    @Override
    protected Void doInBackground(Void... args) {
        topics = noteKeeperDBHelper.getAllTopics();
        bundle.putSerializable(Utils.LIST_TOPICS_KEY, (Serializable) topics);
        notificationManager.notifyLoadedTopics(bundle);
        return null;
    }
}
