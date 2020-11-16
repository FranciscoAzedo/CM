package com.example.challenge2;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.challenge2.model.AsyncTasks.DeleteNoteTask;
import com.example.challenge2.model.AsyncTasks.SaveNoteTask;
import com.example.challenge2.model.NoteContent;
import com.example.challenge2.model.Repository.SharedPreferencesManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.UUID;

public abstract class Utils {

    public static final String SPLIT_STRING_PATTERN = "###";
    public static final String NOTE_CONTENT_FILE_NAME = "noteContents.json";
    public static final String TITLE_LIST_KEY = "titles";
    public static final String NOTE_TITLE_KEY = "noteTitle";
    public static final String NOTE_CONTENT_KEY = "noteContent";
    public static final String NOTE_UUID_KEY = "noteUUID";
    public static final String EDIT_MODE_KEY = "edit";
    public static final String CREATE_NOTE_MODE = "CREATE NOTE";
    public static final String CHANGE_NOTE_TITLE_MODE = "CHANGE NOTE TITLE";
    public static final String CHANGE_NOTE_CONTENT_MODE = "CHANGE NOTE CONTENT";
    public static final String CHANGE_NOTE_TITLE_AND_CONTENT_MODE = "CHANGE NOTE TITLE AND CONTENT";
    public static final String DELETE_NOTE_MODE = "DELETE NOTE";

    public static boolean fileExist(String fileName, Context context) {
//        File file = context.getFileStreamPath(fileName);

        String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        return file.exists();
    }

    public static void updateNotes(String operation, FragmentActivity fragmentActivity, Bundle bundle) throws FileNotFoundException {
        if (operation != null)
            switch (operation) {
                case CREATE_NOTE_MODE:
                    createTitleSharedPreference(fragmentActivity, bundle);
                    createNoteContent(fragmentActivity, bundle);
                    break;
                case CHANGE_NOTE_TITLE_MODE:
                    updateTitleSharedPreference(fragmentActivity, bundle);
                    break;
                case CHANGE_NOTE_CONTENT_MODE:
                    updateNoteContent(fragmentActivity, bundle);
                    break;
                case CHANGE_NOTE_TITLE_AND_CONTENT_MODE:
                    updateTitleSharedPreference(fragmentActivity, bundle);
                    updateNoteContent(fragmentActivity, bundle);
                    break;
                case DELETE_NOTE_MODE:
                    deleteTitleSharedPreference(fragmentActivity, bundle);
                    deleteNoteContent(fragmentActivity, bundle);
                    break;
            }
    }

    public static String getNoteTitle(String uuidNoteTitle) {
        return uuidNoteTitle != null ? uuidNoteTitle.split(Utils.SPLIT_STRING_PATTERN)[0] : null;
    }

    public static UUID getUUIDFromTitle(String noteTitle) {
        return UUID.fromString(noteTitle.split(Utils.SPLIT_STRING_PATTERN)[1]);
    }

    public static String serializeListOfNoteContents(ArrayList<NoteContent> noteContents) {
        return new Gson().toJson(noteContents, new TypeToken<ArrayList<NoteContent>>() {
        }.getType());
    }

    public static ArrayList<NoteContent> deserializeListOfNoteContents(String noteContents) {
        return new Gson().fromJson(noteContents, new TypeToken<ArrayList<NoteContent>>() {
        }.getType());
    }

    private static void createTitleSharedPreference(FragmentActivity fragmentActivity, Bundle bundle) {
        SharedPreferencesManager.saveSharedPreference(
                fragmentActivity,
                TITLE_LIST_KEY,
                bundle.getString(NOTE_TITLE_KEY)
        );
    }

    private static void updateTitleSharedPreference(FragmentActivity fragmentActivity, Bundle bundle) {
        SharedPreferencesManager.updateSharedPreference(
                fragmentActivity,
                TITLE_LIST_KEY,
                bundle.getString(NOTE_TITLE_KEY)
        );
    }

    private static void deleteTitleSharedPreference(FragmentActivity fragmentActivity, Bundle bundle) {
        SharedPreferencesManager.removeSharedPreference(
                fragmentActivity,
                TITLE_LIST_KEY,
                bundle.getString(NOTE_TITLE_KEY)
        );
    }

    private static void createNoteContent(FragmentActivity fragmentActivity, Bundle bundle) {
        new SaveNoteTask(
                fragmentActivity,
                (NoteContent) bundle.getSerializable(NOTE_CONTENT_KEY),
                false
        ).execute();
    }

    private static void updateNoteContent(FragmentActivity fragmentActivity, Bundle bundle) {
        new SaveNoteTask(
                fragmentActivity,
                (NoteContent) bundle.getSerializable(NOTE_CONTENT_KEY),
                true
        ).execute();
    }

    private static void deleteNoteContent(FragmentActivity fragmentActivity, Bundle bundle) {
        new DeleteNoteTask(
                fragmentActivity,
                UUID.fromString(bundle.getString(NOTE_UUID_KEY))
        ).execute();
    }
}
