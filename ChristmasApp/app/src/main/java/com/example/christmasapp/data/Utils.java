package com.example.christmasapp.data;

import android.os.Bundle;

import com.example.christmasapp.data.model.Notification;
import com.example.christmasapp.data.model.NotificationDTO;
import com.example.christmasapp.helpers.DatabaseHelper;
import com.google.gson.Gson;

public abstract class Utils {

    public static final String DATABASE_HELPER_KEY = "DATABASE HELPER";
    
    public static final String CONNECTION_STATUS_KEY = "CONNECTION STATUS";
    
    public static final String OPERATION_KEY = "OPERATION";
    public static final String CREATE_NOTIFICATION_MODE = "CREATE NOTIFICATION";
    public static final String DELETE_NOTIFICATION_MODE = "DELETE NOTIFICATION";
    public static final String NOTIFICATION_KEY = "NOTIFICATION";
    public static final String NOTIFICATION_TITLE_KEY = "NOTIFICATION TITLE";
    public static final String NOTIFICATION_DESCRIPTION_KEY = "NOTIFICATION DESCRIPTION";


    public static boolean updateNotifications(String operation, Bundle bundle) {
        if (operation != null)
            switch (operation) {
                case CREATE_NOTIFICATION_MODE:
                    return createNote(bundle);
                case DELETE_NOTIFICATION_MODE:
                    return deleteNote(bundle);
            }
        return false;
    }

    public static Notification deserializeNotification(String notification) {
        NotificationDTO notificationDTO = new Gson().fromJson(notification, NotificationDTO.class);
        return new Notification(notificationDTO.getTitle(), notificationDTO.getDescription());
    }

    private static boolean createNote(Bundle bundle) {
        DatabaseHelper databaseHelper = (DatabaseHelper) bundle.getSerializable(DATABASE_HELPER_KEY);
        String title = bundle.get(NOTIFICATION_TITLE_KEY) != null ? bundle.get(NOTIFICATION_TITLE_KEY).toString() : null;
        String description = bundle.get(NOTIFICATION_DESCRIPTION_KEY) != null ? bundle.get(NOTIFICATION_DESCRIPTION_KEY).toString() : null;
        Notification notification = new Notification(title, description);
        notification.setId(databaseHelper.addNotification(notification));
        bundle.putSerializable(Utils.NOTIFICATION_KEY, notification);
        return notification.getId() != -1;
    }

    private static boolean deleteNote(Bundle bundle) {
        DatabaseHelper databaseHelper = (DatabaseHelper) bundle.getSerializable(DATABASE_HELPER_KEY);
        Notification notification = (Notification) bundle.getSerializable(NOTIFICATION_KEY);
        return databaseHelper.deleteNotification(notification);
    }
}
