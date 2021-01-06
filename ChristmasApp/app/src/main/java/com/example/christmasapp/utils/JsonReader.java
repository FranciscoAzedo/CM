package com.example.christmasapp.utils;

import com.example.christmasapp.data.model.Event;
import com.example.christmasapp.data.model.PointOfInterest;
import com.example.christmasapp.data.model.Topic;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JsonReader {

    public static List<PointOfInterest> readMonumentsJsonFromUrl(String url) throws IOException {
        InputStream inputStream = new URL(url).openStream();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String jsonText = readAll(bufferedReader);
            Gson gson = new Gson();
            Type poiListType = new TypeToken<ArrayList<PointOfInterest>>(){}.getType();
            return gson.fromJson(jsonText, poiListType);
        }
        finally {
            inputStream.close();
        }
    }

    public static List<Event> readEventsJsonFromUrl(String url) throws IOException {
        InputStream inputStream = new URL(url).openStream();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String jsonText = readAll(bufferedReader);
            Gson gson = new Gson();
            Type poiListType = new TypeToken<ArrayList<Event>>(){}.getType();
            return gson.fromJson(jsonText, poiListType);
        }
        finally {
            inputStream.close();
        }
    }

    public static Set<Topic> serializeTopicList(Set<String> deserializedTopics){
        Set<Topic> topicList = new HashSet<>();
        for (String topic : deserializedTopics)
            topicList.add(deserializeTopic(topic));
        return topicList;
    }

    public static Set<String> deserializeTopicList(Set<Topic> serializedTopics){
        Set<String> topicList = new HashSet<>();
        for (Topic topic : serializedTopics)
            topicList.add(serializeTopic(topic));
        return topicList;
    }

    private static String serializeTopic(Topic topic) {
        return new Gson().toJson(topic, Topic.class);
    }

    private static Topic deserializeTopic(String topic) {
        return new Gson().fromJson(topic, Topic.class);
    }

    private static String readAll(BufferedReader bufferedReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int cp;
        while ((cp = bufferedReader.read()) != -1) {
            stringBuilder.append((char) cp);
        }
        return stringBuilder.toString();

    }
}
