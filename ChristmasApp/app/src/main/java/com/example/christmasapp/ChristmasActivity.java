package com.example.christmasapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.MenuItem;

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
import com.viven.fragmentstatemanager.FragmentStateManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

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

    private PointsOfInterestFragment pointsOfInterestFragment;
    private MapFragment mapFragment = new MapFragment();
    private SubscriptionsFragment subscriptionsFragment = new SubscriptionsFragment();
    private Fragment active = pointsOfInterestFragment;
    private final EventDetailedFragment eventDetailedFragment = new EventDetailedFragment();
    private final MonumentDetailedFragment monumentDetailedFragment = new MonumentDetailedFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);//
//
        changeFragment(PointsOfInterestFragment.newInstance(), PointsOfInterestFragment.class.getSimpleName(), null);

        /* Initialize the database singleton instance */
        NotificationsDbHelper.getInstance(this);
        MqttHelper.getInstance(this);
        SharedPreferencesHelper.getInstance(this);
    }

    public void changeFragment(Fragment fragment, String tagFragmentName, Bundle bundle) {

        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        Fragment currentFragment = mFragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName);
        if (fragmentTemp == null) {
            fragmentTemp = fragment;
            fragmentTransaction.add(R.id.nav_host_fragment, fragmentTemp, tagFragmentName);
        } else {
            fragmentTransaction.show(fragmentTemp);
        }

        fragment.setArguments(bundle);
        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNowAllowingStateLoss();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        int itemID = item.getItemId();

        switch (itemID) {
            case R.id.navigation_points_of_interest:
                changeFragment(new PointsOfInterestFragment(), PointsOfInterestFragment.class
                        .getSimpleName(), null);
                active = pointsOfInterestFragment;
                return true;

            case R.id.navigation_map:
                changeFragment(new MapFragment(), MapFragment.class
                        .getSimpleName(), null);
                active = mapFragment;
                return true;

            case R.id.navigation_subscriptions:
                changeFragment(new SubscriptionsFragment(), SubscriptionsFragment.class
                        .getSimpleName(), null);
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
//            BadgeDrawable badgeDrawable = navView.getOrCreateBadge(R.id.navigation_notifications);
//            badgeDrawable.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.badge_background));
//            badgeDrawable.setBadgeTextColor(Color.WHITE);
//            badgeDrawable.setMaxCharacterCount(2);
//            badgeDrawable.setNumber(++notificationCounter);
//            badgeDrawable.setVisible(true);
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
        changeFragment(notificationsFragment, NotificationsFragment.class.getSimpleName(), null);
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

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.POI_OBJECT_BUNDLE, (Serializable) poi);

        changeFragment(monumentDetailedFragment, MonumentDetailedFragment.class.getSimpleName(), bundle);
        this.active = monumentDetailedFragment;
    }

    @Override
    public void toEventDetails(PointsOfInterestFragment pointsOfInterestFragment, PointOfInterest poi) {
        this.pointsOfInterestFragment = pointsOfInterestFragment;
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.POI_OBJECT_BUNDLE, (Serializable) poi);

        changeFragment(eventDetailedFragment, EventDetailedFragment.class.getSimpleName(), bundle);
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