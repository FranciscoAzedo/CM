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
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.christmasapp.R;
import com.example.christmasapp.data.model.Location;
import com.example.christmasapp.data.model.PointOfInterest;
import com.example.christmasapp.tasks.ReadPointOfInterestInfoTask;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback,
                                                        GoogleMap.OnMapClickListener,
                                                        GoogleMap.OnMyLocationButtonClickListener,
                                                        GoogleMap.OnMyLocationClickListener {

    // List of Points of Interest represented on map
    private List<PointOfInterest> pointOfInterestList;

    // Map object
    private GoogleMap mMap;

    /* [START User Location] */
    // FusedLocationProviderClient required to get user's current location
    private FusedLocationProviderClient mFusedLocationProviderClient;
    // Last-known location retrieved by the Fused Location Provider
    private android.location.Location lastKnownLocation;
    /* [END User Location] */

    /* [START Default Map Configurations] */
    // Default level of map's zoom
    private static final int DEFAULT_ZOOM = 6;
    // Default location (Center of Portugal) to use when location permission is not granted
    private final LatLng defaultLocation = new LatLng(39.694784, -8.130301);
    /* [END Default Map Configurations] */

    /* [START Storing Keys] */
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    /* [END Storing Keys] */

    // Flag indicating location's requested permission status
    private boolean isLocationPermissionGranted = false;
    // Flag indicating location's services status
    private boolean isLocationServiceEnabled = false;

    // Navigation listener
    private MapFragmentListener mapFragmentListener;

    // Broadcast Receiver to handles status changes (enabled/disabled) on location services
    private final BroadcastReceiver gpsReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if(!isLocationPermissionGranted)
                return;

            // Verify if the intent is not null
            if(intent != null) {
                // Get the intent's action
                String action = intent.getAction();

                // If the intent's action refers to a services location change
                if (!TextUtils.isEmpty(action) && action.matches(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                    boolean isEnabled = getLocationServiceStatus();

                    // If the isEnabled flag is different than the already settled
                    if(isEnabled != isLocationServiceEnabled){
                        isLocationServiceEnabled = isEnabled;
                        // Update the interface to insert/remove the user location methods
                        updateUserLocationMethods(isLocationServiceEnabled);
                    }
                }
            }
        }
    };

    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog. Save the return value, an instance of
    // ActivityResultLauncher, as an instance variable.
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission granted
                    // Set location's permission flag as true
                    isLocationPermissionGranted = true;
                    // Update interface to present user location, or user location button
                    updateLocationUI();
                } else {
                    // Permission denied
                    // Set location's permission flag as false
                    isLocationPermissionGranted = false;
                    // Show dialog to warn the user about features that are unavailable because of
                    // his decision. Always respecting the user's decision.
                    showWarningDialog(getContext());
                }
            });

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve location from saved instance state
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }

        // Get reference to map's view element
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        // Initialize the map asynchronously
        supportMapFragment.getMapAsync(this);

        // Initializing a FusedLocationProviderClient
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapFragmentListener.mapActive(this);

        IntentFilter filter = new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION);
        getContext().registerReceiver(gpsReceiver, filter);

        isLocationPermissionGranted = isLocationsPermissionAllowed();
        isLocationServiceEnabled = getLocationServiceStatus();

        if(getLocationServiceStatus() && mMap != null)
            updateLocationUI();

    }

    @Override
    public void onPause() {
        getContext().unregisterReceiver(gpsReceiver);
        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MapFragmentListener) {
            mapFragmentListener = (MapFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNotesListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mapFragmentListener = null;
    }

    public interface MapFragmentListener {
        void mapActive(MapFragment mapFragment);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        new ReadPointOfInterestInfoTask(this).execute();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                latLng, 10
        ));
    }

    @Override
    public boolean onMyLocationButtonClick() {
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull android.location.Location location) {
        // Not used.
    }

    public void fetchPOIs(List<PointOfInterest> pointOfInterestList) {
        this.pointOfInterestList = pointOfInterestList;

        if (mMap != null)
            fetchPOIsOnMap(this.pointOfInterestList);
    }

    public void fetchPOIsOnMap(List<PointOfInterest> pointOfInterestList) {
        for (int i = 0; i < pointOfInterestList.size(); i++) {
            // [SUBSTITUIR]
            pointOfInterestList.get(i).getLocation().setLatitude(39.694784);
            pointOfInterestList.get(i).getLocation().setLongitude(-8.130301);
            setPointOnMap(pointOfInterestList.get(i).getName(), pointOfInterestList.get(i).getLocation());
        }
        //new ReadPOIImageTask(MapFragment.this, pointOfInterestList.get(i).getImageUrl(), i).execute();
    }

    public void fetchImageOnPOI(int index, Bitmap bitmap) {
        final PointOfInterest pointOfInterest = pointOfInterestList.get(index);
        pointOfInterest.setBitmap(bitmap);

        //setPointOnMap(pointOfInterest.getName(), pointOfInterest.getLocation(), bitmap);
    }

    public void setPointOnMap(String title, Location location) {
        LatLng poiLocation = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions marker = new MarkerOptions()
                .position(poiLocation)
                .title(title)
                .icon(bitmapDescriptorFromVector(getContext()));

        mMap.addMarker(marker);

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(poiLocation));
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context/*, @DrawableRes int vectorDrawableResourceId*/) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_poi);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        //Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        //vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        //vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private boolean getLocationServiceStatus(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // This is new method provided in API 28
            // Obtain the reference to the Location Manager
            LocationManager lm = getContext().getSystemService(LocationManager.class);
            // Get the current status of location services
            return lm != null && lm.isLocationEnabled();
        } else {
            // This is Deprecated in API 28
            int mode = Settings.Secure.getInt(getContext().getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            // Get the current status of location services
            return (mode != Settings.Secure.LOCATION_MODE_OFF);
        }
    }

    private void updateUserLocationMethods(boolean flag){
        Toast.makeText(getContext(), "Enabled: " + flag, Toast.LENGTH_SHORT);

        try {
            mMap.setMyLocationEnabled(flag);
            mMap.getUiSettings().setMyLocationButtonEnabled(flag);
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }

        if (isLocationPermissionGranted) {
            updateUserLocationMethods(isLocationServiceEnabled);
        } else {
            updateUserLocationMethods(false);
            lastKnownLocation = null;
            granted();
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (isLocationPermissionGranted) {
                Task<android.location.Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), (OnCompleteListener<android.location.Location>) task -> {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.getResult();

                        if (lastKnownLocation != null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(lastKnownLocation.getLatitude(),
                                            lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else{
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    } else {
                        Log.d("CHRISTMAS APP", "Current location is null. Using defaults.");
                        Log.e("CHRISTMAS APP", "Exception: %s", task.getException());
                        mMap.moveCamera(CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                });
            } else{
                mMap.moveCamera(CameraUpdateFactory
                        .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    public boolean isLocationsPermissionAllowed(){
        return ContextCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED;
    }

    public void granted() {
        if(isLocationPermissionGranted)
            return;

        if (isLocationsPermissionAllowed()) {
            // You can use the API that requires the permission.

            isLocationPermissionGranted = true;
            updateLocationUI();

        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
            showDialog(getContext());
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    public void showDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_location);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        /* Setup Title */
        ((TextView) dialog.findViewById(R.id.tv_permissions_title)).setText(getString(R.string.dialog_request_location_title));
        /* Setup description */
        ((TextView) dialog.findViewById(R.id.tv_permissions_description)).setText(getString(R.string.dialog_request_location_description));

        TextView cancelButton = dialog.findViewById(R.id.tv_permissions_cancel);
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        TextView okButton = dialog.findViewById(R.id.tv_permissions_ok);
        okButton.setOnClickListener(v -> {
            requestPermissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION);
            dialog.dismiss();
        });

        dialog.show();
    }

    public void showWarningDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_location);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        /* Setup Title */
        ((TextView) dialog.findViewById(R.id.tv_permissions_title)).setText(getString(R.string.dialog_location_denied_title));
        /* Setup description */
        ((TextView) dialog.findViewById(R.id.tv_permissions_description)).setText(getString(R.string.dialog_location_denied_description));

        TextView cancelButton = dialog.findViewById(R.id.tv_permissions_cancel);
        cancelButton.setVisibility(View.GONE);

        TextView okButton = dialog.findViewById(R.id.tv_permissions_ok);
        okButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}