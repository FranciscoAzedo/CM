package com.example.christmasapp.tasks;

import android.os.AsyncTask;

import com.example.christmasapp.data.model.Notification;
import com.example.christmasapp.helpers.DatabaseHelper;
import com.example.christmasapp.ui.subscriptions.SubscriptionsFragment;

import java.util.Collections;
import java.util.List;

public class ReadNotificationTask extends AsyncTask<Void, Void, Void> {

    private final DatabaseHelper databaseHelper;
    private final SubscriptionsFragment subscriptionsFragment;

    private List<Notification> notifications;

    public ReadNotificationTask(SubscriptionsFragment subscriptionsFragment) {
        this.databaseHelper = DatabaseHelper.getInstance(null);
        this.subscriptionsFragment = subscriptionsFragment;
    }

    @Override
    protected Void doInBackground(Void... args) {
        notifications = databaseHelper.getAllNotifications();
        Collections.reverse(notifications);
        subscriptionsFragment.updateNotifications(notifications);
        return null;
    }
}
