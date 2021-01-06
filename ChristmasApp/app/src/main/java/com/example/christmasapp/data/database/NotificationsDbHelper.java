package com.example.christmasapp.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.christmasapp.data.model.Notification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.christmasapp.data.database.NotificationsContract.NotificationsEntry.COLUMN_NOTIFICATION_DATE;
import static com.example.christmasapp.data.database.NotificationsContract.NotificationsEntry.COLUMN_NOTIFICATION_DESCRIPTION;
import static com.example.christmasapp.data.database.NotificationsContract.NotificationsEntry.COLUMN_NOTIFICATION_READ;
import static com.example.christmasapp.data.database.NotificationsContract.NotificationsEntry.COLUMN_NOTIFICATION_TITLE;
import static com.example.christmasapp.data.database.NotificationsContract.NotificationsEntry.TABLE_NAME;

/**
 * The class NotificationsDbHelper contains the set of APIs for managing the database. Is main goal
 * is to give references to the database and provide the full set of methods required to interact
 * and perform operations (p.e., insert, update or remove a notification) with the database.
 */
public class NotificationsDbHelper extends SQLiteOpenHelper {

    /**
     * Database's version
     */
    private static final int DATABASE_VERSION = 1;
    /**
     * Database's name
     */
    private static final String DATABASE_NAME = "CHRISTMAS_APP.db";

    /**
     * SQL Query to to create the Notifications table within the database
     */
    private static final String NOTIFICATIONS_QUERY_CREATE =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + NotificationsContract.NotificationsEntry._ID + " TEXT PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_NOTIFICATION_TITLE + " TEXT, "
                    + COLUMN_NOTIFICATION_DESCRIPTION + " TEXT, "
                    + COLUMN_NOTIFICATION_DATE + " DATE, "
                    + COLUMN_NOTIFICATION_READ + " INTEGER);";

    /**
     * SQL Query to drop the Notifications table if exists
     */
    private static final String NOTIFICATIONS_QUERY_DROP =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    /**
     * Singleton instance of the Notifications Database Helper
     */
    private static NotificationsDbHelper dbHelper = null;

    /**
     * Default Constructor
     *
     * @param context the application's context
     */
    // Private to allow singleton instantiation
    private NotificationsDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * @param context
     * @return
     */
    public static NotificationsDbHelper getInstance(Context context) {
        if (dbHelper == null)
            dbHelper = new NotificationsDbHelper(context);

        return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NOTIFICATIONS_QUERY_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the previous version
        db.execSQL(NOTIFICATIONS_QUERY_DROP);
        // Create the new database version
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    /**
     * Gets all notifications within the database
     *
     * @return list of all notifications within the database
     */
    public List<Notification> getAllNotifications() {
        /* Get a readable instance of the database to query */
        SQLiteDatabase db = this.getReadableDatabase();

        /* Get the query results based on the read flag ascending in order to firstly show the
         *  non-read notifications */
        String sortOrder = COLUMN_NOTIFICATION_READ + " ASC";

        /* Query the database and hold the results on a Cursor object */
        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                sortOrder
        );

        /* Initialize the notification's list to return */
        List<Notification> notificationList = new ArrayList<>();

        /* Put the notifications read from the database within the notification's list */
        if (cursor != null && cursor.moveToFirst()) {
            do {
                try {
                    Notification notification = new Notification();
                    notification.setId(cursor.getInt(0));
                    notification.setTitle(cursor.getString(1));
                    notification.setDescription(cursor.getString(2));
                    notification.setDate(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse(cursor.getString(3)));
                    notification.setRead(cursor.getInt(4) > 0);
                    /* Add the previously defined notification to the notification's list */
                    notificationList.add(notification);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        /* Close the opened Cursor object */
        cursor.close();
        db.close();

        return notificationList;
    }

    /**
     * Insert a notification within the database
     *
     * @param notification the notification to be inserted on the database
     * @return result code of the operation
     */
    public int addNotification(Notification notification) {
        /* Get a writable instance of the database to execute an insert query */
        SQLiteDatabase db = this.getWritableDatabase();

        /* Create the map of values, where column names are the keys, corresponding to the new
         *  notification's fields to be inserted within the database*/
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NOTIFICATION_TITLE, notification.getTitle());
        contentValues.put(COLUMN_NOTIFICATION_DESCRIPTION, notification.getDescription());
        contentValues.put(COLUMN_NOTIFICATION_DATE, new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
        contentValues.put(COLUMN_NOTIFICATION_READ, notification.getRead() ? 1 : 0);

        /* Insert the new row */
        final int result = (int) db.insert(TABLE_NAME, null, contentValues);

        /* Close the opened database instance */
        db.close();

        return result;
    }

    /**
     * Set a notification within the database as read
     *
     * @param notificationID the identifier of the notification to be updated as read
     * @return result flag of the operation
     */
    public boolean readNotification(int notificationID) {
        /* Get a writable instance of the database to execute an update query */
        SQLiteDatabase db = this.getWritableDatabase();

        /* Create the map of values, where column names are the keys, corresponding to the
         *  notification's fields to be updated within the database*/
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NOTIFICATION_READ, 1);

        /* Set the selection parameters with which row needs to be updated based on the identifier */
        String selection = NotificationsContract.NotificationsEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(notificationID)};

        /* Update the correspondent row */
        final int result = db.update(TABLE_NAME, contentValues, selection, selectionArgs);

        return result != 0;
    }

    /**
     * Delete a notification from the database
     *
     * @param notificationID the identifier of the notification to be deleted
     * @return result flag of the operation
     */
    public boolean deleteNotification(int notificationID) {
        /* Get a writable instance of the database to execute a drop query */
        SQLiteDatabase db = this.getWritableDatabase();

        /* Set the selection parameters with which row needs to be dropped based on the identifier */
        String selection = NotificationsContract.NotificationsEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(notificationID)};

        /* Drop the correspondent row */
        int result = db.delete(TABLE_NAME, "ID=" + notificationID, null);

        return result != 0;
    }
}
