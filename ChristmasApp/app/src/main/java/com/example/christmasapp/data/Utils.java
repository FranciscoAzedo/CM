package com.example.christmasapp.data;

import android.os.Bundle;

import com.example.christmasapp.data.model.Notification;
import com.example.christmasapp.data.model.NotificationDTO;
import com.example.christmasapp.helpers.DatabaseHelper;
import com.google.gson.Gson;

public abstract class Utils {

    public static final String ACTIVITY_KEY = "ACTIVITY";

    public static final String SUBSCRIPTION_FRAGMENT_KEY = "SUBSCRIPTION FRAGMENT";
    
    public static final String CONNECTION_STATUS_KEY = "CONNECTION STATUS";
    
    public static final String OPERATION_KEY = "OPERATION";
    public static final String CREATE_NOTIFICATION_MODE = "CREATE NOTIFICATION";
    public static final String UPDATE_NOTIFICATION_MODE = "UPDATE NOTIFICATION";
    public static final String DELETE_NOTIFICATION_MODE = "DELETE NOTIFICATION";
    public static final String NOTIFICATION_KEY = "NOTIFICATION";

    public static boolean updateNotifications(String operation, Bundle bundle) {
        if (operation != null)
            switch (operation) {
                case CREATE_NOTIFICATION_MODE:
                    return createNotification(bundle);
                case UPDATE_NOTIFICATION_MODE:
                    return updateNotifications(bundle);
                case DELETE_NOTIFICATION_MODE:
                    return deleteNotification(bundle);
            }
        return false;
    }

    public static NotificationDTO deserializeNotification(String notification) {
        return new Gson().fromJson(notification, NotificationDTO.class);
    }

    private static boolean createNotification(Bundle bundle) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(null);
        NotificationDTO notificationDTO = (NotificationDTO) bundle.getSerializable(NOTIFICATION_KEY);
        Notification notification = new Notification(notificationDTO.getTitle(), notificationDTO.getDescription(), false);
        notification.setId(databaseHelper.addNotification(notification));
        bundle.putSerializable(Utils.NOTIFICATION_KEY, notification);
        return notification.getId() != -1;
    }

    private static boolean updateNotifications(Bundle bundle) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(null);
        Notification notification = (Notification) bundle.getSerializable(NOTIFICATION_KEY);
        return databaseHelper.readNotification(notification);
    }

    private static boolean deleteNotification(Bundle bundle) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(null);
        Notification notification = (Notification) bundle.getSerializable(NOTIFICATION_KEY);
        return databaseHelper.deleteNotification(notification);
    }
}
