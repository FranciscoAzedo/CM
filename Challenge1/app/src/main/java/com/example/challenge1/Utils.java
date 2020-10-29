package com.example.challenge1;

import android.content.res.Resources;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public abstract class Utils {
    public static int getResourceByName(Resources resources, String resourceName, String resourceType, String packageName) {
        return resources.getIdentifier(resourceName.toLowerCase(), resourceType, packageName);
    }

    public static String serializeListOfAnimals(ArrayList<Animal> animals){
        return new Gson().toJson(animals, new TypeToken<ArrayList<Animal>>() {}.getType());
    }

    public static String serializeAnimal(Animal animal){
        return new Gson().toJson(animal, Animal.class);
    }

    public static ArrayList<Animal> deserializeListOfAnimals(String animals){
        return new Gson().fromJson(animals, new TypeToken<ArrayList<Animal>>() {}.getType());
    }

    public static Animal deserializeAnimal(String animal){
        return new Gson().fromJson(animal, Animal.class);
    }
}
