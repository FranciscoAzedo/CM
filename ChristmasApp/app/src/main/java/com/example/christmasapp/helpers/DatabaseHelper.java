package com.example.christmasapp.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.christmasapp.data.model.Notification;
import com.example.christmasapp.data.model.Topic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper implements Serializable {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "CHRISTMAS_APP.db";

    private static final String GET_ALL_FROM_TABLE_QUERY = "SELECT * FROM ";

    private static final String NOTIFICATION_TABLE_NAME = "notification";
    private static final String COLUMN_NOTIFICATION_TITLE = "NOTIFICATION_TITLE";
    private static final String COLUMN_NOTIFICATION_CONTENT = "NOTIFICATION_CONTENT";
    private static final String NOTIFICATION_TABLE_CREATE = "CREATE TABLE " + NOTIFICATION_TABLE_NAME + " ("
            + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NOTIFICATION_TITLE + " TEXT, "
            + COLUMN_NOTIFICATION_CONTENT + " TEXT);";

    private static final String TOPIC_TABLE_NAME = "topic";
    private static final String COLUMN_TOPIC_NAME = "TOPIC_NAME";
    private static final String TOPIC_TABLE_CREATE = "CREATE TABLE " + TOPIC_TABLE_NAME + " ("
            + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TOPIC_NAME + " TEXT);";


    public DatabaseHelper(Context context) {
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
                Notification notification = new Notification();
                notification.setId(cursor.getInt(0));
                notification.setTitle(cursor.getString(1));
                notification.setDescription(cursor.getString(2));
                notificationList.add(notification);
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
        contentValues.put(COLUMN_NOTIFICATION_CONTENT, notification.getDescription());

        final int result = (int) database.insert(NOTIFICATION_TABLE_NAME, null, contentValues);
        database.close();

        return result;
    }

    public boolean deleteNotification(Notification notification) {
        SQLiteDatabase database = this.getWritableDatabase();

        int result = database.delete(NOTIFICATION_TABLE_NAME, "ID=" + notification.getId() + " AND " + COLUMN_NOTIFICATION_TITLE + "='" + notification.getTitle() + "' AND " + COLUMN_NOTIFICATION_CONTENT + "='" + notification.getDescription() + "'", null);

        return result != 0;
    }

    public List<Topic> getAllTopics() {
        List<Topic> topicList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        String getAllNotificationsQuery = GET_ALL_FROM_TABLE_QUERY + TOPIC_TABLE_NAME;

        Cursor cursor = database.rawQuery(getAllNotificationsQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Topic topic = new Topic();
                topic.setId(cursor.getInt(0));
                topic.setName(cursor.getString(1));
                topicList.add(topic);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();
        return topicList;
    }

    public int addTopic(Topic topic) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_TOPIC_NAME, topic.getName());

        final int result = (int) database.insert(TOPIC_TABLE_NAME, null, contentValues);
        database.close();

        return result;
    }

    public boolean deleteTopic(Topic topic) {
        SQLiteDatabase database = this.getWritableDatabase();

        int result = database.delete(TOPIC_TABLE_NAME, "ID=" + topic.getId() + " AND " + COLUMN_TOPIC_NAME + "='" + topic.getName() + "'", null);

        return result != 0;
    }
}
