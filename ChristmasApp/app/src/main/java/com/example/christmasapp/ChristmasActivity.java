package com.example.christmasapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import com.example.christmasapp.data.database.NotificationsDbHelper;
import com.example.christmasapp.data.model.PointOfInterest;
import com.example.christmasapp.helpers.SharedPreferencesHelper;
import com.example.christmasapp.ui.pois.Event_Detailed.EventDetailedFragment;
import com.example.christmasapp.ui.pois.Monument_Detailed.MonumentDetailedFragment;
import com.example.christmasapp.utils.Constants;
import com.example.christmasapp.helpers.MqttHelper;
import com.example.christmasapp.tasks.ReadNotificationTask;
import com.example.christmasapp.tasks.ReadTopicTask;
import com.example.christmasapp.ui.map.MapFragment;
import com.example.christmasapp.ui.subscriptions.SubscriptionsFragment;
import com.example.christmasapp.ui.pois.PointsOfInterestFragment;
import com.example.christmasapp.ui.subscriptions.notifications.NotificationsFragment;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ui.AppBarConfiguration;

import java.io.Serializable;
import java.util.Map;

import static com.example.christmasapp.utils.Constants.REQUEST_LOCATION;

public class ChristmasActivity extends AppCompatActivity implements NotificationManager,
        MapFragment.MapFragmentListener,
        SubscriptionsFragment.SubscriptionsFragmentListener,
                                                            PointsOfInterestFragment.PointsOfInterestFragmentListener,
        NotificationsFragment.NotificationFragmentListener,
                                                            Serializable {

    private BottomNavigationView navView;

    private BadgeDrawable badgeDrawable;
    private int notificationCounter = 0;

    private NotificationsFragment notificationsFragment = new NotificationsFragment();

    private PointsOfInterestFragment pointsOfInterestFragment = new PointsOfInterestFragment();
    private MapFragment mapFragment = new MapFragment();
    private SubscriptionsFragment subscriptionsFragment = new SubscriptionsFragment();
    private Fragment active = pointsOfInterestFragment;
    private EventDetailedFragment eventDetailedFragment = new EventDetailedFragment();
    private MonumentDetailedFragment monumentDetailedFragment = new MonumentDetailedFragment();

    private FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//
//
        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, mapFragment, "map_frag").hide(mapFragment).commit();
        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, notificationsFragment, "not_frag").hide(notificationsFragment).commit();
        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, subscriptionsFragment, "sub_frag").hide(subscriptionsFragment).commit();
        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, eventDetailedFragment, "eve_frag").hide(eventDetailedFragment).commit();
        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, monumentDetailedFragment, "mon_frag").hide(monumentDetailedFragment).commit();
        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, pointsOfInterestFragment, "poi_frag").commit();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_points_of_interest, R.id.navigation_map, R.id.navigation_subscriptions)
                .build();

//        NavHostFragment navHostFragment =
//                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
//        NavController navController = navHostFragment.getNavController();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);
//        badgeDrawable = navView.getOrCreateBadge(R.id.navigation_notifications);
//        badgeDrawable.setVisible(false);

        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        /* Initialize the database singleton instance */
        NotificationsDbHelper.getInstance(this);
        MqttHelper.getInstance(this);
        SharedPreferencesHelper.getInstance(this);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_points_of_interest:
                fragmentManager.beginTransaction().hide(active).show(pointsOfInterestFragment).commit();
                active = pointsOfInterestFragment;
                return true;

            case R.id.navigation_map:
                fragmentManager.beginTransaction().hide(active).show(mapFragment).commit();
                active = mapFragment;
                return true;

            case R.id.navigation_subscriptions:
                fragmentManager.beginTransaction().hide(active).show(subscriptionsFragment).commit();
                active = subscriptionsFragment;
                return true;
        }
        return false;
    };


    @Override
    protected void onResume() {
        super.onResume();

        /* Guarantee the connection to the database is available */
        NotificationsDbHelper.getInstance(this);
    }

    @Override
    protected void onDestroy() {
        /* Close the database connection */
        NotificationsDbHelper.getInstance(this).close();

        super.onDestroy();
    }

    @Override
    public void notifyConnection(Bundle bundle) {
        Boolean connectionStatus = bundle.getBoolean(Constants.CONNECTION_STATUS_KEY);

//        bundle.putSerializable(Constants.ACTIVITY_KEY, this);
//        bundle.putString(Constants.TOPIC_KEY, "CM_TP_2020");
//        new SaveTopicTask(bundle).execute();

        if (connectionStatus)
            updateTopicsList();
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
        if (active instanceof SubscriptionsFragment)
            updateTopicsList();

//        else {
//            BadgeDrawable badgeDrawable = navView.getOrCreateBadge(R.id.navigation_subscriptions);
//            badgeDrawable.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.badge_background));
//            badgeDrawable.setBadgeTextColor(Color.WHITE);
//            badgeDrawable.setMaxCharacterCount(2);
//            badgeDrawable.setNumber(++notificationCounter);
//            badgeDrawable.setVisible(true);
//        }
    }

    @Override
    public void notifyDeletedTopic(Bundle bundle) {
        updateTopicsList();
    }

    private void addNewNotification(){
        if (active instanceof NotificationsFragment)
            new ReadNotificationTask(notificationsFragment).execute();
        else {
            BadgeDrawable badgeDrawable = navView.getOrCreateBadge(R.id.navigation_notifications);
            badgeDrawable.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.badge_background));
            badgeDrawable.setBadgeTextColor(Color.WHITE);
            badgeDrawable.setMaxCharacterCount(2);
            badgeDrawable.setNumber(++notificationCounter);
            badgeDrawable.setVisible(true);
        }
    }

    @Override
    public void notificationsActive(NotificationsFragment notificationsFragment) {
        this.notificationsFragment = notificationsFragment;
        this.active = notificationsFragment;
        notificationCounter = 0;
//        badgeDrawable.setVisible(false);
    }

    @Override
    public void mapActive(MapFragment mapFragment) {
        this.mapFragment = mapFragment;
        this.active = mapFragment;
    }

    @Override
    public void subscriptionsActive(SubscriptionsFragment subscriptionsFragment) {
        this.subscriptionsFragment = subscriptionsFragment;
        this.active = subscriptionsFragment;
    }

    @Override
    public void notificationsActive(SubscriptionsFragment subscriptionsFragment) {
        this.subscriptionsFragment = subscriptionsFragment;
        fragmentManager.beginTransaction().hide(active).show(notificationsFragment).commit();
        this.active = notificationsFragment;
    }

    @Override
    public void pointsOfInterestActive(PointsOfInterestFragment pointsOfInterestFragment) {
        this.pointsOfInterestFragment = pointsOfInterestFragment;
        this.active = pointsOfInterestFragment;
    }

    @Override
    public void toMonumentDetails(PointsOfInterestFragment pointsOfInterestFragment, PointOfInterest poi) {
        this.pointsOfInterestFragment = pointsOfInterestFragment;
        fragmentManager.beginTransaction().hide(active).show(monumentDetailedFragment).commit();
        this.active = monumentDetailedFragment;
    }

    @Override
    public void toEventDetails(PointsOfInterestFragment pointsOfInterestFragment, PointOfInterest poi) {
        this.pointsOfInterestFragment = pointsOfInterestFragment;
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.POI_OBJECT_BUNDLE, (Serializable) poi);
        eventDetailedFragment.setArguments(bundle);
        fragmentManager.beginTransaction().hide(active).show(eventDetailedFragment).commit();

        this.active = eventDetailedFragment;
    }

    private void updateNotificationsView(Bundle bundle){
        NotificationsFragment notificationsFragment = (NotificationsFragment) bundle.getSerializable(Constants.NOTIFICATION_FRAGMENT_KEY);
        new ReadNotificationTask(notificationsFragment).execute();
    }
    private void updateTopicsList() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.ACTIVITY_KEY, this);
        new ReadTopicTask(subscriptionsFragment, bundle).execute();

    }
}