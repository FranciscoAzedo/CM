package com.example.christmasapp;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ui.AppBarConfiguration;

import java.io.Serializable;

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

    private PointsOfInterestFragment pointsOfInterestFragment;
    private MapFragment mapFragment;
    private SubscriptionsFragment subscriptionsFragment;
    private Fragment activeFragment = pointsOfInterestFragment;
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
//        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, mapFragment, "map_frag").hide(mapFragment).commit();
//        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, notificationsFragment, "not_frag").hide(notificationsFragment).commit();
//        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, subscriptionsFragment, "sub_frag").hide(subscriptionsFragment).commit();
//        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, eventDetailedFragment, "eve_frag").hide(eventDetailedFragment).commit();
//        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, monumentDetailedFragment, "mon_frag").hide(monumentDetailedFragment).commit();
//        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, pointsOfInterestFragment, "pointsOfInterestFragment").commit();

        fragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, PointsOfInterestFragment.newInstance(), "pointsOfInterestFragment")
                .addToBackStack(null)
                .commit();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_points_of_interest, R.id.navigation_map, R.id.navigation_subscriptions)
                .build();


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
                        if ((pointsOfInterestFragment = (PointsOfInterestFragment) fragmentManager.findFragmentByTag("pointsOfInterestFragment")) == null)
                            pointsOfInterestFragment = PointsOfInterestFragment.newInstance();

                        fragmentManager.beginTransaction()
                                .replace(R.id.nav_host_fragment, pointsOfInterestFragment, "pointsOfInterestFragment")
                                .addToBackStack("pointsOfInterestFragment")
                                .commit();
                        activeFragment = pointsOfInterestFragment;
                        return true;

                    case R.id.navigation_map:
                        if ((mapFragment = (MapFragment) fragmentManager.findFragmentByTag("mapFragment")) == null)
                            mapFragment = MapFragment.newInstance();

                        fragmentManager.beginTransaction()
                                .replace(R.id.nav_host_fragment, mapFragment, "mapFragment")
                                .addToBackStack("mapFragment")
                                .commit();
                        activeFragment = mapFragment;
                        return true;

                    case R.id.navigation_subscriptions:
                        if ((subscriptionsFragment = (SubscriptionsFragment) fragmentManager.findFragmentByTag("subscriptionsFragment")) == null)
                            subscriptionsFragment = SubscriptionsFragment.newInstance();

                        fragmentManager.beginTransaction()
                                .replace(R.id.nav_host_fragment, subscriptionsFragment, "subscriptionsFragment")
                                .addToBackStack(null)
                                .commit();
                        activeFragment = subscriptionsFragment;
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
        if (activeFragment instanceof SubscriptionsFragment)
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
        if (activeFragment instanceof NotificationsFragment)
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
        this.activeFragment = notificationsFragment;
        notificationCounter = 0;
//        badgeDrawable.setVisible(false);
    }

    @Override
    public void mapActive(MapFragment mapFragment) {
        this.mapFragment = mapFragment;
        this.activeFragment = mapFragment;
    }

    @Override
    public void subscriptionsActive(SubscriptionsFragment subscriptionsFragment) {
        this.subscriptionsFragment = subscriptionsFragment;
        this.activeFragment = subscriptionsFragment;
    }

    @Override
    public void notificationsActive(SubscriptionsFragment subscriptionsFragment) {
        this.subscriptionsFragment = subscriptionsFragment;
        fragmentManager.beginTransaction().hide(activeFragment).show(notificationsFragment).commit();
        this.activeFragment = notificationsFragment;
    }

    @Override
    public void pointsOfInterestActive(PointsOfInterestFragment pointsOfInterestFragment) {
        this.pointsOfInterestFragment = pointsOfInterestFragment;
        this.activeFragment = pointsOfInterestFragment;
    }

    @Override
    public void toMonumentDetails(PointsOfInterestFragment pointsOfInterestFragment, PointOfInterest poi) {
        this.pointsOfInterestFragment = pointsOfInterestFragment;
        fragmentManager.beginTransaction().hide(activeFragment).show(monumentDetailedFragment).commit();
        this.activeFragment = monumentDetailedFragment;
    }

    @Override
    public void toEventDetails(PointsOfInterestFragment pointsOfInterestFragment, PointOfInterest poi) {
        this.pointsOfInterestFragment = pointsOfInterestFragment;
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.POI_OBJECT_BUNDLE, (Serializable) poi);
        eventDetailedFragment.setArguments(bundle);
        fragmentManager.beginTransaction().hide(activeFragment).show(eventDetailedFragment).commit();

        this.activeFragment = eventDetailedFragment;
    }

    private void updateNotificationsView(Bundle bundle){
        NotificationsFragment notificationsFragment = (NotificationsFragment) bundle.getSerializable(Constants.NOTIFICATION_FRAGMENT_KEY);
        new ReadNotificationTask(notificationsFragment).execute();
    }
    private void updateTopicsList(){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.ACTIVITY_KEY, this);
        new ReadTopicTask(subscriptionsFragment, bundle).execute();
    }

}