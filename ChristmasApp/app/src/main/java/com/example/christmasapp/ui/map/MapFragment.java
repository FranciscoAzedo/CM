package com.example.christmasapp.ui.map;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.christmasapp.R;
import com.example.christmasapp.data.model.Event;
import com.example.christmasapp.data.model.Location;
import com.example.christmasapp.data.model.PointOfInterest;
import com.example.christmasapp.tasks.ReadPointOfInterestInfoTask;
import com.example.christmasapp.utils.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback,
                                                        GoogleMap.OnMarkerClickListener,
                                                        GoogleMap.OnInfoWindowClickListener {

    /* [START Data Objects] */
    // List of Points of Interest represented on map
    private List<PointOfInterest> pointOfInterestList;
    // Map object
    private GoogleMap mMap;
    // Last-known camera position
    private CameraPosition lastCameraPosition;
    /* [END Data Objects] */

    /* [START User Location] */
    // FusedLocationProviderClient required to get user's current location
    private FusedLocationProviderClient mFusedLocationProviderClient;
    // Last-known location retrieved by the Fused Location Provider
    private android.location.Location lastKnownLocation;
    /* [END User Location] */

    /* [START Default Map Configurations] */
    // Default level of map's zoom
    private static final int DEFAULT_ZOOM = 6;
    // Default level of map's zoom when a marker is clicked
    private static final int DEFAULT_MARKER_ZOOM = 9;
    // Default location (Center of Portugal)
    private final LatLng defaultLocation = new LatLng(39.694784, -8.130301);
    /* [END Default Map Configurations] */

    /* [START Storing Keys] */
    // Key for storing/retrieving the camera position from the savedInstanceState
    private static final String KEY_CAMERA_POSITION = "camera_position";
    // Key for storing/retrieving the current user location from the savedInstanceState
    private static final String KEY_USER_LOCATION = "user_location";
    /* [END Storing Keys] */

    /* [START Permissions] */
    // Flag indicating location's requested permission status
    private boolean isLocationPermissionGranted = false;
    // Flag indicating location's services status
    private boolean isLocationServiceEnabled = false;
    // Flag indicating connectivity status
    private boolean isNetworkAvailable = false;
    /* [END Permissions/Services] */

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    /* [START Navigation items] */
    // Navigation listener
    private MapFragmentListener mapFragmentListener;
    // Navigation interface
    public interface MapFragmentListener {
        void mapActive(MapFragment mapFragment);
        void toEventDetails(MapFragment mapFragment, PointOfInterest poi);
        void toMonumentDetails(MapFragment mapFragment, PointOfInterest poi);
    }
    /* [END Navigation items] */

    // Broadcast Receiver to handles status changes (enabled/disabled) on location services
    private final BroadcastReceiver gpsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // When the location permission are not granted doesn't worth to execute this method
            if (!isLocationPermissionGranted)
                return;

            if (intent != null) {
                String action = intent.getAction();

                // If the intent's action refers to a services location change
                if (!TextUtils.isEmpty(action) && action.matches(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                    // Get the current services location status
                    boolean isEnabled = isLocationServicesEnabled();

                    // If the isEnabled flag is different than the already settled
                    // (this verification is done since the listener enables the action 4x by change)
                    if (isEnabled != isLocationServiceEnabled) {
                        // Update the location services status flag
                        isLocationServiceEnabled = isEnabled;
                        // Update the interface to insert/remove the user location methods
                        updateUserLocationMethods(isLocationServiceEnabled);
                    }
                }
            }
        }
    };

    // Broadcast Receiver to handles connectivity changes (enabled/disabled)
    private final BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();

                // Update connectivity flag according to the current connectivity status
                if(!TextUtils.isEmpty(action) && action.matches("android.net.conn.CONNECTIVITY_CHANGE")) {
                    isNetworkAvailable = Utils.isOnline(getContext());

                    // Get the list of points of interest asynchronously
                    if (isNetworkAvailable && pointOfInterestList == null)
                        new ReadPointOfInterestInfoTask(MapFragment.this).execute();
                }
            }
        }
    };

    // Register the permissions callback, which handles the user's response to the system
    // permissions dialog. Save the return value on an instance of ActivityResultLauncher
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Set location's permission flag as true
                    isLocationPermissionGranted = true;
                    // Update interface to present user location, or user location button
                    updateUserLocationUI();
                } else {
                    // Set location's permission flag as false
                    isLocationPermissionGranted = false;
                    // Show dialog to warn the user about features that are unavailable because of
                    // his decision. Always respecting the user's decision.
                    showPermissionDeniedDialog(getContext());
                }
            });

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve last location and camera position from saved instance state
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_USER_LOCATION);
            lastCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Get reference to map's view element
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        // Initialize the map asynchronously
        if(supportMapFragment != null)
            supportMapFragment.getMapAsync(this);

        // Initializing a FusedLocationProviderClient
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
    }

    /**
     * Saves the state of the map when the activity is paused.
     *
     * @param outState bundle to save information
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        // Store last location and camera position on outState
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_USER_LOCATION, lastKnownLocation);
        }

        super.onSaveInstanceState(outState);
    }

    /**
     * Registers broadcast receivers and resume the permissions status.
     */
    @Override
    public void onResume() {
        super.onResume();
        mapFragmentListener.mapActive(this);

        // Register broadcast receiver to be aware of changes on location status
        IntentFilter filter = new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION);
        requireContext().registerReceiver(gpsReceiver, filter);
        // Register broadcast receiver to be aware of changes on connectivity status
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        requireContext().registerReceiver(networkReceiver, intentFilter);

        // Get the current location permission and service status
        isLocationPermissionGranted = isLocationsPermissionGranted();
        isLocationServiceEnabled = isLocationServicesEnabled();
        // If the location services are enabled when the view is resumed, update the map's interface
        // (added to change the interface when users came back to the app with the permission enabled)
        if (mMap != null && isLocationServiceEnabled)
            updateUserLocationUI();
    }

    /**
     * Unregisters broadcast receivers
     */
    @Override
    public void onPause() {
        // Unregister the broadcast receivers
        requireContext().unregisterReceiver(gpsReceiver);
        requireContext().unregisterReceiver(networkReceiver);

        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MapFragmentListener) {
            mapFragmentListener = (MapFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMapFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mapFragmentListener = null;
    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     *
     * @param googleMap map
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Set map listeners
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);

        // Turn on the My Location layer and the related control on the map.
        updateUserLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        // Get the list of points of interest asynchronously
        if(isNetworkAvailable)
            new ReadPointOfInterestInfoTask(this).execute();
    }

    /**
     * Centered a marker, and possible zooming in, when selected.
     *
     * @param marker selected marker
     * @return default behavior flag
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        // If the marker was clicked and is not null zooming in to the marker's position
        if (marker != null && mMap.getCameraPosition().zoom < DEFAULT_MARKER_ZOOM) {
            mMap.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(marker.getPosition(), DEFAULT_MARKER_ZOOM));
        }

        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the marker's centred position).
        return false;
    }

    /**
     * Redirect the user to the POI's detailed window.
     *
     * @param marker the marker that the user clicked on title
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        if (marker == null)
            return;

        // Get the marker's tag where are being saved his title
        String marketTitle = (String) marker.getTag();

        for (int i = 0; i < pointOfInterestList.size(); i++) {
            PointOfInterest poi = pointOfInterestList.get(i);

            // Get the correspondent POI to that marker's title
            if (poi.getName().equals(marketTitle)) {
                // Redirect the user to the correct fragment according to the POI's type
                if (poi instanceof Event)
                    mapFragmentListener.toEventDetails(MapFragment.this, poi);
                else
                    mapFragmentListener.toMonumentDetails(MapFragment.this, poi);
            }
        }
    }

    /**
     * Fetch the POIs on the list and on the map.
     *
     * @param pointOfInterestList list of POIs to be fetched
     */
    public void fetchPOIsOnMap(List<PointOfInterest> pointOfInterestList) {
        // Fetch the POI's list
        this.pointOfInterestList = pointOfInterestList;

        // Fetch the Map with markers on the POIs locations
        if (mMap != null)
            for(PointOfInterest poi : pointOfInterestList)
                addMarkerOnMap(poi.getName(), poi.getLocation());
    }

    /**
     * Setup a marker on map with POI's location.
     *
     * @param title the POI's title to be assigned as marker's tag
     * @param location the POI's location to be assigned to the map marker
     */
    public void addMarkerOnMap(String title, Location location) {
        // Instantiate a Google Maps Location to fetch a map marker on the POI's location
        LatLng poiLocation = new LatLng(location.getLatitude(), location.getLongitude());

        // Instantiate the POI's map marker configuration
        MarkerOptions markerOptions = new MarkerOptions()
                .position(poiLocation)
                .title(title)
                .icon(bitmapDescriptorFromVector(getContext()));

        // Add the marker to the map
        Marker marker = mMap.addMarker(markerOptions);
        // Save the POI's title on the map marker tag
        // (to the application know the POI's on the info window click)
        marker.setTag(title);
    }

    /**
     * Gets a BitmapDescriptor from a Vector Asset (found on Github).
     * This allows us to set a customized map marker by getting the BitmapDescriptor.
     *
     * @param context application's context
     * @return Vector's Asset BitmapDescriptor
     */
    private BitmapDescriptor bitmapDescriptorFromVector(Context context) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_poi);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    /**
     * Updates the map's user location interface according to a flag.
     *
     * @param flag the status to be assigned to google maps user's location interface
     */
    private void updateUserLocationMethods(boolean flag) {
        try {
            // Sets the user's location on map
            mMap.setMyLocationEnabled(flag);
            // Sets the user's location button on map
            mMap.getUiSettings().setMyLocationButtonEnabled(flag);
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateUserLocationUI() {
        if (mMap == null)
            return;

        if (isLocationPermissionGranted) {
            updateUserLocationMethods(isLocationServiceEnabled);
        } else {
            updateUserLocationMethods(false);
            lastKnownLocation = null;
            getLocationPermission();
        }
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available
         */
        try {
            if (isLocationPermissionGranted) {
                // Execute a task to get the last user's location
                Task<android.location.Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener((OnCompleteListener<android.location.Location>) task -> {
                    if (task.isSuccessful()) {
                        // Get the last user's location
                        lastKnownLocation = task.getResult();
                        // Set the map's camera position to the current location of the device
                        if (lastKnownLocation != null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(lastKnownLocation.getLatitude(),
                                            lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            // Set the map's camera position to the default location of the device
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                        }
                    } else {
                        // Set the map's camera position to the default location of the device
                        mMap.moveCamera(CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                    }
                });
            } else {
                mMap.moveCamera(CameraUpdateFactory
                        .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    /**
     * Gets the location's permission status
     *
     * @return location's permission status
     */
    public boolean isLocationsPermissionGranted() {
        return ContextCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Gets the location's service status.
     *
     * @return location's service status
     */
    private boolean isLocationServicesEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Obtain the reference to the Location Manager
            LocationManager lm = getContext().getSystemService(LocationManager.class);
            // Get the current status of location services
            return lm != null && lm.isLocationEnabled();
        } else {
            // Deprecated in API 28
            int mode = Settings.Secure.getInt(getContext().getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            // Get the current status of location services
            return (mode != Settings.Secure.LOCATION_MODE_OFF);
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    public void getLocationPermission() {
        if (isLocationPermissionGranted)
            return;

        if (isLocationsPermissionGranted()) {
            // Update the location's permission flag
            isLocationPermissionGranted = true;
            // Update the user's location map interface
            updateUserLocationUI();

        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
            showPermissionRequestDialog(getContext());
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Shows an alert dialog to request user's location permission
     *
     * @param context application's context
     */
    public void showPermissionRequestDialog(Context context) {
        // Instantiate the dialog view
        final Dialog dialog = new Dialog(context);
        // Set up the dialog configuration
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_location);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Setup Title
        ((TextView) dialog.findViewById(R.id.tv_permissions_title)).setText(getString(R.string.dialog_request_location_title));
        // Setup description
        ((TextView) dialog.findViewById(R.id.tv_permissions_description)).setText(getString(R.string.dialog_request_location_description));
        // Setup cancel button behaviour
        TextView cancelButton = dialog.findViewById(R.id.tv_permissions_cancel);
        cancelButton.setOnClickListener(v -> dialog.dismiss());
        // Setup ok button behaviour
        TextView okButton = dialog.findViewById(R.id.tv_permissions_ok);
        okButton.setOnClickListener(v -> {
            // Request permission
            requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION);
            dialog.dismiss();
        });

        // Show the dialog
        dialog.show();
    }

    /**
     * Shows an alert dialog to warn the user that by not grant locations permission some
     * functionalities may be disabled
     *
     * @param context
     */
    public void showPermissionDeniedDialog(Context context) {
        // Instantiate the dialog view
        final Dialog dialog = new Dialog(context);
        // Set up the dialog configuration
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_location);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Setup Title
        ((TextView) dialog.findViewById(R.id.tv_permissions_title)).setText(getString(R.string.dialog_location_denied_title));
        // Setup description
        ((TextView) dialog.findViewById(R.id.tv_permissions_description)).setText(getString(R.string.dialog_location_denied_description));
        // Setup cancel button, hiding him
        TextView cancelButton = dialog.findViewById(R.id.tv_permissions_cancel);
        cancelButton.setVisibility(View.GONE);
        // Setup ok button behaviour
        TextView okButton = dialog.findViewById(R.id.tv_permissions_ok);
        okButton.setOnClickListener(v -> dialog.dismiss());

        // Show the dialog
        dialog.show();
    }
}