package com.example.christmasapp.utils;

import android.os.Bundle;

import com.example.christmasapp.data.database.NotificationsDbHelper;
import com.example.christmasapp.data.model.Notification;
import com.example.christmasapp.data.model.dto.NotificationDTO;
import com.example.christmasapp.data.model.Topic;
import com.example.christmasapp.helpers.SharedPreferencesHelper;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        NotificationsDbHelper notificationsDbHelper = NotificationsDbHelper.getInstance(null);
        NotificationDTO notificationDTO = (NotificationDTO) bundle.getSerializable(Constants.NOTIFICATION_KEY);
        Notification notification = new Notification(notificationDTO.getTitle(), notificationDTO.getDescription(), false);
        notification.setId(notificationsDbHelper.addNotification(notification));
        bundle.putSerializable(Constants.NOTIFICATION_KEY, notification);
        return notification.getId() != -1;
    }

    private static boolean updateNotifications(Bundle bundle) {
        NotificationsDbHelper notificationsDbHelper = NotificationsDbHelper.getInstance(null);
        Notification notification = (Notification) bundle.getSerializable(Constants.NOTIFICATION_KEY);
        return notificationsDbHelper.readNotification(notification.getId());
    }

    private static boolean deleteNotification(Bundle bundle) {
        NotificationsDbHelper notificationsDbHelper = NotificationsDbHelper.getInstance(null);
        Notification notification = (Notification) bundle.getSerializable(Constants.NOTIFICATION_KEY);
        return notificationsDbHelper.deleteNotification(notification.getId());
    }

    private static boolean createTopic(Bundle bundle) {
        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(null);
        sharedPreferencesHelper.saveSharedPreference(Constants.SHARED_PREFERENCES_TOPIC_KEY,
                                                        (Topic) bundle.getSerializable(Constants.TOPIC_KEY));
        return true;
    }

    private static boolean deleteTopic(Bundle bundle) {
        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(null);
        sharedPreferencesHelper.removeSharedPreference(Constants.SHARED_PREFERENCES_TOPIC_KEY,
                                                        (Topic) bundle.getSerializable(Constants.TOPIC_KEY));
        return true;
    }

    public static String getNotificationTime(Notification notification) {
        Date currentTime = Calendar.getInstance().getTime();
        String timeValue = "";

        try {
            long differenceLong = currentTime.getTime() - notification.getDate().getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date differenceDate = simpleDateFormat.parse(simpleDateFormat.format(differenceLong));
            Calendar differenceCalendar = Calendar.getInstance();
            differenceCalendar.setTime(differenceDate);

            if (differenceCalendar.get(Calendar.YEAR) > 1970){
                timeValue += (differenceCalendar.get(Calendar.YEAR) - 1970) + " years ago";
            } else {
                if (differenceCalendar.get(Calendar.MONTH) > 0){
                    timeValue += differenceCalendar.get(Calendar.MONTH) + " months ago";
                } else {
                    if (differenceCalendar.get(Calendar.DAY_OF_MONTH) > 1){
                        timeValue += (differenceCalendar.get(Calendar.DAY_OF_MONTH) - 1) + " days ago";
                    } else {
                        if (differenceCalendar.get(Calendar.HOUR_OF_DAY) > 0){
                            timeValue += differenceCalendar.get(Calendar.HOUR_OF_DAY) + " hours ago";
                        } else {
                            if (differenceCalendar.get(Calendar.MINUTE) > 0){
                                timeValue += differenceCalendar.get(Calendar.MINUTE) + " minutes ago";
                            } else {
                                timeValue += "just now";
                            }
                        }
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timeValue;
    }
}
