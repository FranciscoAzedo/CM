package com.example.challenge2;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.challenge2.model.AsyncTasks.SaveNoteTask;
import com.example.challenge2.model.Repository.FileSystemManager;
import com.example.challenge2.model.Repository.SharedPreferencesManager;

import java.util.UUID;

public abstract class Utils {
    public static String generateUUID() {
        return String.valueOf(UUID.randomUUID());
    }

    public static String getNoteTitle(String uuidNoteTitle) {
        return uuidNoteTitle != null ? uuidNoteTitle.split("-")[0] : null;
    }

    public static void updateNotes(String operation, FragmentActivity fragmentActivity, Bundle bundle) {
        switch (operation) {
            case "CREATE NOTE":
                createTitleSharedPreference(fragmentActivity, bundle);
                createContentFile(fragmentActivity, bundle);
                break;
            case "CHANGE NOTE":
                createTitleSharedPreference(fragmentActivity, bundle);
                createContentFile(fragmentActivity, bundle);
                deleteTitleSharedPreference(fragmentActivity, bundle);
                deleteContentFile(fragmentActivity, bundle);
                break;
            case "CHANGE CONTENT":
                createContentFile(fragmentActivity, bundle);
                break;
            case "DELETE NOTE":
                deleteTitleSharedPreference(fragmentActivity, bundle);
                deleteContentFile(fragmentActivity, bundle);
                break;
        }
    }

    private static void createTitleSharedPreference(FragmentActivity fragmentActivity, Bundle bundle) {
        SharedPreferencesManager.saveSharedPreference(
                fragmentActivity,
                "titles",
                bundle.getString("uuidTitle"));
    }

    private static void createContentFile(FragmentActivity fragmentActivity, Bundle bundle) {
        new SaveNoteTask(
                fragmentActivity,
                bundle.getString("uuidTitle"),
                bundle.getString("content")).execute();
    }

    private static void deleteTitleSharedPreference(FragmentActivity fragmentActivity, Bundle bundle) {
        SharedPreferencesManager.removeSharedPreference(
                fragmentActivity,
                "titles",
                bundle.getString("title"));
    }

    private static void deleteContentFile(FragmentActivity fragmentActivity, Bundle bundle) {
        FileSystemManager.removeNoteFile(
                fragmentActivity,
                bundle.getString("title"));
    }
}
