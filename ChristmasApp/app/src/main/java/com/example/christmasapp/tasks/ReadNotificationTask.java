package com.example.christmasapp.tasks;

import android.os.AsyncTask;

import com.example.christmasapp.data.model.Notification;
import com.example.christmasapp.helpers.DatabaseHelper;
import com.example.christmasapp.ui.notifications.NotificationsFragment;

import java.util.Collections;
import java.util.List;

public class ReadNotificationTask extends AsyncTask<Void, Void, Void> {

    private final DatabaseHelper databaseHelper;
    private final NotificationsFragment notificationsFragment;

    private List<Notification> notifications;

    public ReadNotificationTask(NotificationsFragment notificationsFragment) {
        this.databaseHelper = DatabaseHelper.getInstance(null);
        this.notificationsFragment = notificationsFragment;
    }

    @Override
    protected Void doInBackground(Void... args) {
        notifications = databaseHelper.getAllNotifications();
        Collections.reverse(notifications);
        notificationsFragment.updateNotifications(notifications);
        return null;
    }
}
