package com.example.christmasapp.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.christmasapp.data.model.Notification;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper implements Serializable {

    private static DatabaseHelper databaseHelper = null;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "CHRISTMAS_APP.db";

    private static final String GET_ALL_FROM_TABLE_QUERY = "SELECT * FROM ";

    private static final String NOTIFICATION_TABLE_NAME = "notification";
    private static final String COLUMN_NOTIFICATION_TITLE = "NOTIFICATION_TITLE";
    private static final String COLUMN_NOTIFICATION_DESCRIPTION = "NOTIFICATION_DESCRIPTION";
    private static final String COLUMN_NOTIFICATION_DATE = "NOTIFICATION_DATE";
    private static final String COLUMN_NOTIFICATION_READ = "NOTIFICATION_READ";
    private static final String NOTIFICATION_TABLE_CREATE = "CREATE TABLE " + NOTIFICATION_TABLE_NAME + " ("
            + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NOTIFICATION_TITLE + " TEXT, "
            + COLUMN_NOTIFICATION_DESCRIPTION + " TEXT, "
            + COLUMN_NOTIFICATION_DATE + " DATE, "
            + COLUMN_NOTIFICATION_READ + " INTEGER);";

    private static final String TOPIC_TABLE_NAME = "topic";
    private static final String COLUMN_TOPIC_NAME = "TOPIC_NAME";
    private static final String TOPIC_TABLE_CREATE = "CREATE TABLE " + TOPIC_TABLE_NAME + " ("
            + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TOPIC_NAME + " TEXT);";


    public static DatabaseHelper getInstance(Context context) {
        if (databaseHelper == null)
        {
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseHelper;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(NOTIFICATION_TABLE_CREATE);
        sqLiteDatabase.execSQL(TOPIC_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public List<Notification> getAllNotifications() {
        List<Notification> notificationList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        String getAllNotificationsQuery = GET_ALL_FROM_TABLE_QUERY + NOTIFICATION_TABLE_NAME;

        Cursor cursor = database.rawQuery(getAllNotificationsQuery, null);

        if (cursor.moveToFirst()) {
            do {
                try {
                    Notification notification = new Notification();
                    notification.setId(cursor.getInt(0));
                    notification.setTitle(cursor.getString(1));
                    notification.setDescription(cursor.getString(2));
                    notification.setDate(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse(cursor.getString(3)));
                    notification.setRead(cursor.getInt(4) > 0);
                    notificationList.add(notification);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();
        return notificationList;
    }

    public int addNotification(Notification notification) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NOTIFICATION_TITLE, notification.getTitle());
        contentValues.put(COLUMN_NOTIFICATION_DESCRIPTION, notification.getDescription());
        contentValues.put(COLUMN_NOTIFICATION_DATE, new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
        contentValues.put(COLUMN_NOTIFICATION_READ, notification.getRead()? 1 : 0);

        final int result = (int) database.insert(NOTIFICATION_TABLE_NAME, null, contentValues);
        database.close();

        return result;
    }

    public boolean readNotification(Notification notification) {
        int result;
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NOTIFICATION_TITLE, notification.getTitle());
        contentValues.put(COLUMN_NOTIFICATION_DESCRIPTION, notification.getDescription());
        contentValues.put(COLUMN_NOTIFICATION_READ, 1);

        result = database.update(NOTIFICATION_TABLE_NAME, contentValues, "ID = " + notification.getId(), null);

        return result != 0;
    }

    public boolean deleteNotification(Notification notification) {
        SQLiteDatabase database = this.getWritableDatabase();

        int result = database.delete(NOTIFICATION_TABLE_NAME, "ID=" + notification.getId(), null);

        return result != 0;
    }

//    public List<Topic> getAllTopics() {
//        List<Topic> topicList = new ArrayList<>();
//        SQLiteDatabase database = this.getReadableDatabase();
//        String getAllNotificationsQuery = GET_ALL_FROM_TABLE_QUERY + TOPIC_TABLE_NAME;
//
//        Cursor cursor = database.rawQuery(getAllNotificationsQuery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                Topic topic = new Topic();
//                topic.setId(cursor.getInt(0));
//                topic.setName(cursor.getString(1));
//                topicList.add(topic);
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
//        database.close();
//        return topicList;
//    }
//
//    public int addTopic(Topic topic) {
//        SQLiteDatabase database = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//
//        contentValues.put(COLUMN_TOPIC_NAME, topic.getName());
//
//        final int result = (int) database.insert(TOPIC_TABLE_NAME, null, contentValues);
//        database.close();
//
//        return result;
//    }
//
//    public boolean deleteTopic(Topic topic) {
//        SQLiteDatabase database = this.getWritableDatabase();
//
//        int result = database.delete(TOPIC_TABLE_NAME, "ID=" + topic.getId(), null);
//
//        return result != 0;
//    }
}
