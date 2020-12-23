package com.example.christmasapp;

import android.graphics.Color;
import android.os.Bundle;

import com.example.christmasapp.helpers.DatabaseHelper;
import com.example.christmasapp.helpers.MqttHelper;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class ChristmasActivity extends AppCompatActivity implements NotificationManager {

    private DatabaseHelper databaseHelper;
    private MqttHelper mqttHelper;
    private BottomNavigationView navView;
    private int notificationCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_points_of_interest, R.id.navigation_map, R.id.navigation_history, R.id.navigation_subscriptions)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        databaseHelper = new DatabaseHelper(this);
        mqttHelper = new MqttHelper(databaseHelper, this);


    }

    @Override
    public void notifyConnection(Bundle bundle) {
        mqttHelper.subscribeToTopic("CM_TP_2020");
    }

    @Override
    public void notifyLoadedNotes(Bundle bundle) {

    }

    @Override
    public void notifyNewNote(Bundle bundle) {

    }

    @Override
    public void notifyUpdateNote(Bundle bundle) {

    }

    @Override
    public void notifyDeletedNote(Bundle bundle) {

    }

    @Override
    public void newNotification(Bundle bundle) {
        BadgeDrawable badgeDrawable = navView.getOrCreateBadge(R.id.navigation_subscriptions);
        badgeDrawable.setBackgroundColor(Color.BLUE);
        badgeDrawable.setBadgeTextColor(Color.YELLOW);
        badgeDrawable.setMaxCharacterCount(2);
        badgeDrawable.setNumber(notificationCounter++);
        badgeDrawable.setVisible(true);
    }

    @Override
    public void notifyLoadedTopics(Bundle bundle) {

    }

    @Override
    public void notifyNewTopic(Bundle bundle) {

    }

    @Override
    public void notifyDeletedTopic(Bundle bundle) {

    }
}