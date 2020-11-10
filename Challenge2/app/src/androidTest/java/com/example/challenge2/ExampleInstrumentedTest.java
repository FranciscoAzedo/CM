package com.example.challenge2;

import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.challenge2.model.Repository.SharedPreferencesManager;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.challenge2", appContext.getPackageName());
    }

    @Test
    public void teste() {
//        stringSet.add("tobias");

//        FileSystemManager.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferencesManager.saveSharedPreference(appContext, "titles", "merda");
//        SharedPreferencesManager.saveSharedPreference(appContext, "titles", "merdinhas");

        Set<String> set = SharedPreferencesManager.getSharedPreference(appContext, "titles");

        for (String s : set) {
            Log.d("TAG", s);
        }
    }
}