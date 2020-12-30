package com.example.christmasapp.utils;

import com.example.christmasapp.data.model.PointOfInterest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class JsonReader {

    public static List<PointOfInterest> readJsonFromUrl(String url) throws IOException {
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

    private static String readAll(BufferedReader bufferedReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int cp;
        while ((cp = bufferedReader.read()) != -1) {
            stringBuilder.append((char) cp);
        }
        return stringBuilder.toString();

    }
}
