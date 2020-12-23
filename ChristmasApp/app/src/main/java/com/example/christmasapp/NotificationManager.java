package com.example.christmasapp;

import android.os.Bundle;

public interface NotificationManager {
    void notifyConnection(Bundle bundle);

    void notifyLoadedNotes(Bundle bundle);

    void notifyNewNote(Bundle bundle);

    void notifyUpdateNote(Bundle bundle);

    void notifyDeletedNote(Bundle bundle);

    void newNotification(Bundle bundle);

    void notifyLoadedTopics(Bundle bundle);

    void notifyNewTopic(Bundle bundle);

    void notifyDeletedTopic(Bundle bundle);
}
