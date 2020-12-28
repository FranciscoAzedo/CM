package com.example.christmasapp;

import android.os.Bundle;

public interface NotificationManager {
    void notifyConnection(Bundle bundle);

    void notifyNewNote(Bundle bundle);

    void notifyUpdateNote(Bundle bundle);

    void notifyUpdatedNotification(Bundle bundle);

    void notifyDeletedNotification(Bundle bundle);

    void newNotification(Bundle bundle);

    void notifyLoadedNotifications(Bundle bundle);

    void notifyLoadedTopics(Bundle bundle);

    void notifyNewTopic(Bundle bundle);

    void notifyDeletedTopic(Bundle bundle);
}
