package com.example.challenge2.model.Repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.challenge2.R;

public class SharedPreferencesManager {

    public static String getSharedPreference(Context context, final String key, final String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name),
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }

    public static void saveSharedPreference(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.app_name),
                Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }
}