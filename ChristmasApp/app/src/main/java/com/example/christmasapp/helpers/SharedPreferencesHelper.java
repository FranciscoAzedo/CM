package com.example.christmasapp.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.christmasapp.R;
import com.example.christmasapp.data.model.Topic;
import com.example.christmasapp.utils.JsonReader;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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

    public List<Topic> getSharedPreference(final String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name),
                Context.MODE_PRIVATE);
        Set<String> sharedPreferencesResult = sharedPreferences.getStringSet(key, Collections.emptySet());

        if (!sharedPreferencesResult.isEmpty())
            return JsonReader.serializeTopicList(sharedPreferencesResult);
        return Collections.emptyList();
    }

    public void saveSharedPreference(String key, Topic topic) {
        Set<Topic> topicsSet = new HashSet<>(getSharedPreference(key));
        topicsSet.add(topic);
        Set<String> stringSet = JsonReader.deserializeTopicList(topicsSet);

        SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.app_name),
                Context.MODE_PRIVATE).edit();
        editor.putStringSet(key, stringSet);
        editor.apply();
    }

    public void removeSharedPreference(String key, Topic topic) {
        Set<Topic> topicsSet = new HashSet<>(getSharedPreference(key));

        for (Topic auxTopic : topicsSet) {
            if(auxTopic.getName().equals(topic.getName()) && auxTopic.getImageUrl().equals(topic.getImageUrl())) {
                topicsSet.remove(auxTopic);
                break;
            }
        }
        Set<String> stringSet = JsonReader.deserializeTopicList(topicsSet);

        SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.app_name),
                Context.MODE_PRIVATE).edit();
        editor.putStringSet(key, stringSet);
        editor.apply();
    }

    public int getNotifications(final String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name),
                Context.MODE_PRIVATE);
        int sharedPreferencesResult = sharedPreferences.getInt(key, 0);
        return sharedPreferencesResult;
    }

    public void setNotifications(String key, int notifications) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.app_name),
                Context.MODE_PRIVATE).edit();
        editor.putInt(key, notifications);
        editor.apply();
    }
}