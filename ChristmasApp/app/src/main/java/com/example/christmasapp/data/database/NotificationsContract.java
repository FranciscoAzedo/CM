package com.example.christmasapp.data.database;

import android.provider.BaseColumns;

/**
 * The class NotificationsContract was developed as a good practise on SQL databases that is the
 * schema definition. It is a companion class that explicitly specifies the layout of notification's
 * schema in a systematic and self-documenting way. (As mentioned by Android Developers)
 */
public class NotificationsContract {

    /**
     * Default Constructor
     */
    private NotificationsContract() {
        // Defined as private to prevent someone from accidentally instantiating the contract class
    }

    /**
     * The inner class NotificationsEntry defines the Notifications table contents (i.e., the schema)
     */
    public static class NotificationsEntry implements BaseColumns {
        public static final String TABLE_NAME = "notification";
        public static final String COLUMN_NOTIFICATION_TITLE = "NOTIFICATION_TITLE";
        public static final String COLUMN_NOTIFICATION_DESCRIPTION = "NOTIFICATION_DESCRIPTION";
        public static final String COLUMN_NOTIFICATION_DATE = "NOTIFICATION_DATE";
        public static final String COLUMN_NOTIFICATION_READ = "NOTIFICATION_READ";
    }
}

