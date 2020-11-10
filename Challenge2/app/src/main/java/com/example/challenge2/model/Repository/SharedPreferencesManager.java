package com.example.challenge2.model.Repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.challenge2.R;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class SharedPreferencesManager {

    public static Set<String> getSharedPreference(Context context, final String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name),
                Context.MODE_PRIVATE);
        return sharedPreferences.getStringSet(key, Collections.emptySet());
    }

    public static void saveSharedPreference(Context context, String key, String value) {
        Set<String> stringSet = new HashSet<>(getSharedPreference(context, key));
        stringSet.add(value);

        SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.app_name),
                Context.MODE_PRIVATE).edit();
        editor.putStringSet(key, stringSet);
        editor.commit();
    }

    public static void removeSharedPreference(Context context, String key, String value) {
        Set<String> stringSet = new HashSet<>(getSharedPreference(context, key));
        stringSet.remove(value);

        SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.app_name),
                Context.MODE_PRIVATE).edit();
        editor.putStringSet(key, stringSet);
        editor.commit();
    }
}