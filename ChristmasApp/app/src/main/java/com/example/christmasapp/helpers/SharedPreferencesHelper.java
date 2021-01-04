package com.example.christmasapp.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.christmasapp.R;
import com.example.christmasapp.utils.Utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class SharedPreferencesHelper {

    private final Context context;
    private static SharedPreferencesHelper sharedPreferencesHelper;

    protected SharedPreferencesHelper(Context context) {
        this.context = context;
    }

    public static SharedPreferencesHelper getInstance(Context context) {
        if (sharedPreferencesHelper == null)
        {
            sharedPreferencesHelper = new SharedPreferencesHelper(context);
        }
        return sharedPreferencesHelper;
    }

    public Set<String> getSharedPreference(final String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name),
                Context.MODE_PRIVATE);
        return sharedPreferences.getStringSet(key, Collections.emptySet());
    }

    public void saveSharedPreference(String key, String value) {
        Set<String> stringSet = new HashSet<>(getSharedPreference(key));
        stringSet.add(value);

        SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.app_name),
                Context.MODE_PRIVATE).edit();
        editor.putStringSet(key, stringSet);
        editor.apply();
    }

    public void updateSharedPreference(String key, String value) {
        Set<String> stringSet = new HashSet<>(getSharedPreference(key));
        for (String title : stringSet)
//            if (title.contains(String.valueOf(Utils.getUUIDFromTitle(value)))) {
//                stringSet.remove(title);
//                stringSet.add(value);
                break;
//            }
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.app_name),
                Context.MODE_PRIVATE).edit();
        editor.putStringSet(key, stringSet);
        editor.apply();
    }

    public void removeSharedPreference(String key, String value) {
        Set<String> stringSet = new HashSet<>(getSharedPreference(key));
        stringSet.remove(value);

        SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.app_name),
                Context.MODE_PRIVATE).edit();
        editor.putStringSet(key, stringSet);
        editor.apply();
    }
}