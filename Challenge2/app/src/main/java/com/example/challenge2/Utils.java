package com.example.challenge2;

import android.os.Bundle;

import com.example.challenge2.model.Note;
import com.example.challenge2.model.Repository.NoteKeeperDBHelper;
import com.google.gson.Gson;

public abstract class Utils {


    public static final String NOTE_LIST_KEY = "noteList";
    public static final String NOTE_KEY = "note";
    public static final String TOPIC_KEY = "topic";
    public static final String DATABASE_HELPER_KEY = "databaseHelper";
    public static final String MQTT_HELPER_KEY = "mqttHelper";
    public static final String PUBLISH_TOPIC_KEY = "azedoTopic";
    public static final String CONNECTION_STATUS_KEY = "connectionStatus";
    public static final String CONNECTION_KEY = "connection";
    public static final String ACTIVITY_KEY = "activity";
    public static final String OPERATION_KEY = "operation";
    public static final String LIST_TOPICS_KEY = "listTopics";
    public static final String LIST_NOTES_KEY = "listNotes";
    public static final String LIST_SEARCH_NOTES_KEY = "listSearchNotes";
    public static final String NOTE_TITLE_KEY = "noteTitle";
    public static final String NOTE_CONTENT_KEY = "noteContent";
    public static final String EDIT_MODE_KEY = "edit";
    public static final String CREATE_NOTE_MODE = "CREATE NOTE";
    public static final String CHANGE_NOTE_MODE = "CHANGE NOTE";
    public static final String DELETE_NOTE_MODE = "DELETE NOTE";

    public static boolean updateNotes(String operation, Bundle bundle) {
        if (operation != null)
            switch (operation) {
                case CREATE_NOTE_MODE:
                    return createNote(bundle);
                case CHANGE_NOTE_MODE:
                    return updateNote(bundle);
                case DELETE_NOTE_MODE:
                    return deleteNote(bundle);
            }
        return false;
    }

    private static boolean createNote(Bundle bundle) {
        NoteKeeperDBHelper noteKeeperDBHelper = (NoteKeeperDBHelper) bundle.getSerializable(DATABASE_HELPER_KEY);
        String title = bundle.get(NOTE_TITLE_KEY) != null ? bundle.get(NOTE_TITLE_KEY).toString() : null;
        String content = bundle.get(NOTE_CONTENT_KEY) != null ? bundle.get(NOTE_CONTENT_KEY).toString() : null;
        Note note = new Note(title, content);
        note.setId(noteKeeperDBHelper.addNote(note));
        bundle.putSerializable(Utils.NOTE_KEY, note);
        return note.getId() != -1;
    }

    private static boolean updateNote(Bundle bundle) {
        NoteKeeperDBHelper noteKeeperDBHelper = (NoteKeeperDBHelper) bundle.getSerializable(DATABASE_HELPER_KEY);
        Note note = (Note) bundle.getSerializable(NOTE_KEY);
        String title = bundle.getString(NOTE_TITLE_KEY);
        String content = bundle.getString(NOTE_CONTENT_KEY);
        return noteKeeperDBHelper.updateNote(note, title, content);
    }

    private static boolean deleteNote(Bundle bundle) {
        NoteKeeperDBHelper noteKeeperDBHelper = (NoteKeeperDBHelper) bundle.getSerializable(DATABASE_HELPER_KEY);
        Note note = (Note) bundle.getSerializable(NOTE_KEY);
        return noteKeeperDBHelper.deleteNote(note);
    }

    public static String serializeNote(Note note) {
        return new Gson().toJson(note, Note.class);
    }

    public static Note deserializeAnimal(String note) {
        return new Gson().fromJson(note, Note.class);
    }
}
