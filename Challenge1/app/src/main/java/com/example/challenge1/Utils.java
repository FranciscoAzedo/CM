package com.example.challenge1;

import android.content.res.Resources;

public abstract class Utils {
    public static int getResourceByName(Resources resources, String resourceName, String resourceType, String packageName) {
        return resources.getIdentifier(resourceName.toLowerCase(), resourceType, packageName);
    }
}
