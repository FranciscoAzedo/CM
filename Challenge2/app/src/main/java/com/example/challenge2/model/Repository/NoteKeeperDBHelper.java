package com.example.challenge2.model.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.challenge2.model.Note;
import com.example.challenge2.model.Topic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NoteKeeperDBHelper extends SQLiteOpenHelper implements Serializable {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "CM_CHALLENGE_2_MILESTONE_2.db";

    private static final String GET_ALL_FROM_TABLE_QUERY = "SELECT * FROM ";

    private static final String NOTE_TABLE_NAME = "note";
    private static final String COLUMN_NOTE_TITLE = "NOTE_TITLE";
    private static final String COLUMN_NOTE_CONTENT = "NOTE_CONTENT";
    private static final String NOTE_TABLE_CREATE = "CREATE TABLE " + NOTE_TABLE_NAME + " ("
            + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NOTE_TITLE + " TEXT, "
            + COLUMN_NOTE_CONTENT + " TEXT);";

    private static final String TOPIC_TABLE_NAME = "topic";
    private static final String COLUMN_TOPIC_NAME = "TOPIC_NAME";
    private static final String TOPIC_TABLE_CREATE = "CREATE TABLE " + TOPIC_TABLE_NAME + " ("
            + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TOPIC_NAME + " TEXT);";


    public NoteKeeperDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(NOTE_TABLE_CREATE);
        sqLiteDatabase.execSQL(TOPIC_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

//    public Note getNoteByIdAndTitle(int id, String title){
//        Note note = null;
//        SQLiteDatabase database = this.getReadableDatabase();
//        String getNoteQuery = GET_ALL_FROM_TABLE_QUERY + NOTE_TABLE_NAME + " WHERE ID=" + id + " AND " + COLUMN_NOTE_TITLE + "='" + title + "'";
//
//        Cursor cursor = database.rawQuery(getNoteQuery, null);
//        if (cursor.moveToFirst()){
//            note = new Note();
//            note.setId(cursor.getInt(0));
//            note.setTitle(cursor.getString(1));
//            note.setContent(cursor.getString(2));
//        }
//        return note;
//    }

    public List<Note> getAllNotes() {
        List<Note> noteList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        String getAllNotesQuery = GET_ALL_FROM_TABLE_QUERY + NOTE_TABLE_NAME;

        Cursor cursor = database.rawQuery(getAllNotesQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(0));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                noteList.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();
        return noteList;
    }

    public int addNote(Note note) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NOTE_TITLE, note.getTitle());
        contentValues.put(COLUMN_NOTE_CONTENT, note.getContent());

        final int result = (int) database.insert(NOTE_TABLE_NAME, null, contentValues);
        database.close();

        return result;
    }

    public boolean updateNote(Note note, String title, String content) {
        int result;
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        if (title != null)
            contentValues.put(COLUMN_NOTE_TITLE, title);

        if (content != null)
            contentValues.put(COLUMN_NOTE_CONTENT, content);

        result = database.update(NOTE_TABLE_NAME, contentValues, "ID = " + note.getId() + " AND " + COLUMN_NOTE_TITLE + "='" + note.getTitle() + "' AND " + COLUMN_NOTE_CONTENT + "='" + note.getContent() + "'", null);

        return result != 0;
    }

    public boolean deleteNote(Note note) {
        SQLiteDatabase database = this.getWritableDatabase();

        int result = database.delete(NOTE_TABLE_NAME, "ID=" + note.getId() + " AND " + COLUMN_NOTE_TITLE + "='" + note.getTitle() + "' AND " + COLUMN_NOTE_CONTENT + "='" + note.getContent() + "'", null);

        return result != 0;
    }

    public List<Topic> getAllTopics() {
        List<Topic> topicList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        String getAllNotesQuery = GET_ALL_FROM_TABLE_QUERY + TOPIC_TABLE_NAME;

        Cursor cursor = database.rawQuery(getAllNotesQuery, null);

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
