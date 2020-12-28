package com.example.christmasapp.tasks;

import android.os.AsyncTask;
import android.os.Bundle;

import com.example.christmasapp.ChristmasActivity;
import com.example.christmasapp.NotificationManager;
import com.example.christmasapp.data.Utils;

public class UpdateNotificationTask extends AsyncTask<Void, Void, Void> {

    private final Bundle bundle;
    private final NotificationManager notificationManager;
    private boolean result;

    public UpdateNotificationTask(Bundle bundle) {
        this.bundle = bundle;
        this.notificationManager = (ChristmasActivity) bundle.getSerializable(Utils.ACTIVITY_KEY);
    }

    @Override
    protected void onPostExecute(Void arg) {
        if (result)
            notificationManager.notifyUpdatedNotification(bundle);
    }

    @Override
    protected Void doInBackground(Void... args) {
        result = Utils.updateNotifications(Utils.UPDATE_NOTIFICATION_MODE, bundle);
        return null;
    }
}
