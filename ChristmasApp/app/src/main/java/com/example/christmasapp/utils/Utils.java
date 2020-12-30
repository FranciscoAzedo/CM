package com.example.christmasapp.utils;

import android.os.Bundle;

import com.example.christmasapp.data.model.Notification;
import com.example.christmasapp.data.model.NotificationDTO;
import com.example.christmasapp.data.model.Topic;
import com.example.christmasapp.helpers.DatabaseHelper;
import com.google.gson.Gson;

public abstract class Utils {



    public static boolean updateNotifications(String operation, Bundle bundle) {
        if (operation != null)
            switch (operation) {
                case Constants.CREATE_NOTIFICATION_MODE:
                    return createNotification(bundle);
                case Constants.UPDATE_NOTIFICATION_MODE:
                    return updateNotifications(bundle);
                case Constants.DELETE_NOTIFICATION_MODE:
                    return deleteNotification(bundle);
            }
        return false;
    }

    public static NotificationDTO deserializeNotification(String notification) {
        return new Gson().fromJson(notification, NotificationDTO.class);
    }

    public static boolean updateTopics(String operation, Bundle bundle) {
        if (operation != null)
            switch (operation) {
                case Constants.CREATE_TOPIC_MODE:
                    return createTopic(bundle);
                case Constants.DELETE_TOPIC_MODE:
                    return deleteTopic(bundle);
            }
        return false;
    }

    private static boolean createNotification(Bundle bundle) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(null);
        NotificationDTO notificationDTO = (NotificationDTO) bundle.getSerializable(Constants.NOTIFICATION_KEY);
        Notification notification = new Notification(notificationDTO.getTitle(), notificationDTO.getDescription(), false);
        notification.setId(databaseHelper.addNotification(notification));
        bundle.putSerializable(Constants.NOTIFICATION_KEY, notification);
        return notification.getId() != -1;
    }

    private static boolean updateNotifications(Bundle bundle) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(null);
        Notification notification = (Notification) bundle.getSerializable(Constants.NOTIFICATION_KEY);
        return databaseHelper.readNotification(notification);
    }

    private static boolean deleteNotification(Bundle bundle) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(null);
        Notification notification = (Notification) bundle.getSerializable(Constants.NOTIFICATION_KEY);
        return databaseHelper.deleteNotification(notification);
    }

    private static boolean createTopic(Bundle bundle) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(null);
        Topic topic = (Topic) bundle.getSerializable(Constants.TOPIC_KEY);
        topic.setId(databaseHelper.addTopic(topic));
        bundle.putSerializable(Constants.TOPIC_KEY, topic);
        return topic.getId() != -1;
    }

    private static boolean deleteTopic(Bundle bundle) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(null);
        Topic topic = (Topic) bundle.getSerializable(Constants.TOPIC_KEY);
        return databaseHelper.deleteTopic(topic);
    }
}
