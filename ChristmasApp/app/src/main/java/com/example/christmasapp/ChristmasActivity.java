package com.example.christmasapp;

import android.graphics.Color;
import android.os.Bundle;

import com.example.christmasapp.data.Utils;
import com.example.christmasapp.helpers.DatabaseHelper;
import com.example.christmasapp.helpers.MqttHelper;
import com.example.christmasapp.tasks.ReadNotificationTask;
import com.example.christmasapp.ui.history.HistoryFragment;
import com.example.christmasapp.ui.map.MapFragment;
import com.example.christmasapp.ui.points_of_interest.PointsOfInterestFragment;
import com.example.christmasapp.ui.subscriptions.SubscriptionsFragment;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.Serializable;

public class ChristmasActivity extends AppCompatActivity implements NotificationManager,
                                                            HistoryFragment.HistoryFragmentListener,
                                                            MapFragment.MapFragmentListener,
                                                            PointsOfInterestFragment.PointsOfInterestFragmentListener,
                                                            SubscriptionsFragment.SubscriptionFragmentListener,
                                                            Serializable {

    private BottomNavigationView navView;

    private BadgeDrawable badgeDrawable;
    private int notificationCounter = 0;

    private SubscriptionsFragment subscriptionsFragment;
    private HistoryFragment historyFragment;
    private MapFragment mapFragment;
    private PointsOfInterestFragment pointsOfInterestFragment;
    private Fragment active;

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
        badgeDrawable = navView.getOrCreateBadge(R.id.navigation_subscriptions);
        badgeDrawable.setVisible(false);

        DatabaseHelper.getInstance(this);
        MqttHelper.getInstance(this);
    }

    @Override
    public void notifyConnection(Bundle bundle) {
        MqttHelper.getInstance(this).subscribeToTopic("CM_TP_2020");
    }

    @Override
    public void notifyNewNote(Bundle bundle) {

    }

    @Override
    public void notifyUpdateNote(Bundle bundle) {

    }

    @Override
    public void notifyUpdatedNotification(Bundle bundle) {
        updateNotificationsView(bundle);
    }

    @Override
    public void notifyDeletedNotification(Bundle bundle) {
        updateNotificationsView(bundle);
    }

    @Override
    public void newNotification(Bundle bundle) {
        addNewNotification();
    }

    @Override
    public void notifyLoadedNotifications(Bundle bundle) {

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

    private void addNewNotification(){

        if (active instanceof SubscriptionsFragment)
            new ReadNotificationTask(subscriptionsFragment).execute();
        else {
            BadgeDrawable badgeDrawable = navView.getOrCreateBadge(R.id.navigation_subscriptions);
            badgeDrawable.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.badge_background));
            badgeDrawable.setBadgeTextColor(Color.WHITE);
            badgeDrawable.setMaxCharacterCount(2);
            badgeDrawable.setNumber(++notificationCounter);
            badgeDrawable.setVisible(true);
        }
    }

    @Override
    public void notificationsActive(SubscriptionsFragment subscriptionsFragment) {
        this.subscriptionsFragment = subscriptionsFragment;
        this.active = subscriptionsFragment;
        notificationCounter = 0;
        badgeDrawable.setVisible(false);
    }

    @Override
    public void historyActive(HistoryFragment historyFragment) {
        this.historyFragment = historyFragment;
        this.active = historyFragment;
    }

    @Override
    public void mapActive(MapFragment mapFragment) {
        this.mapFragment = mapFragment;
        this.active = mapFragment;
    }

    @Override
    public void pointsOfInterestActive(PointsOfInterestFragment pointsOfInterestFragment) {
        this.pointsOfInterestFragment = pointsOfInterestFragment;
        this.active = pointsOfInterestFragment;
    }

    private void updateNotificationsView(Bundle bundle){
        SubscriptionsFragment subscriptionsFragment = (SubscriptionsFragment) bundle.getSerializable(Utils.SUBSCRIPTION_FRAGMENT_KEY);
        new ReadNotificationTask(subscriptionsFragment).execute();
    }
}