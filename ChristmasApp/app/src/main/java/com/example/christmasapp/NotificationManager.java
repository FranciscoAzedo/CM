package com.example.christmasapp;

import android.os.Bundle;

public interface NotificationManager {
    void notifyConnection(Bundle bundle);

    void notifyUpdatedNotification(Bundle bundle);

    void notifyDeletedNotification(Bundle bundle);

    void newNotification(Bundle bundle);

    void notifyNewTopic(Bundle bundle);

    void notifyDeletedTopic(Bundle bundle);
}
