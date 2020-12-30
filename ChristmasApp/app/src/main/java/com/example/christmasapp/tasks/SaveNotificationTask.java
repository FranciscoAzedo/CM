package com.example.christmasapp.tasks;

import android.os.AsyncTask;
import android.os.Bundle;

import com.example.christmasapp.ChristmasActivity;
import com.example.christmasapp.NotificationManager;
import com.example.christmasapp.utils.Constants;
import com.example.christmasapp.utils.Utils;

public class SaveNotificationTask extends AsyncTask<Void, Void, Void> {

    private final Bundle bundle;
    private final NotificationManager notificationManager;
    private boolean result;

    public SaveNotificationTask(Bundle bundle) {
        this.bundle = bundle;
        this.notificationManager = (ChristmasActivity) bundle.getSerializable(Constants.ACTIVITY_KEY);
    }

    @Override
    protected void onPostExecute(Void arg) {
        if (result)
            notificationManager.newNotification(bundle);
    }

    @Override
    protected Void doInBackground(Void... args) {
        result = Utils.updateNotifications(Constants.CREATE_NOTIFICATION_MODE, bundle);
        return null;
    }
}