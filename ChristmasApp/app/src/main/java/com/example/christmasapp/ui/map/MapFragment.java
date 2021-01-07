package com.example.christmasapp.ui.map;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.example.christmasapp.data.model.Location;
import com.example.christmasapp.data.model.PointOfInterest;
import com.example.christmasapp.tasks.ReadPointOfInterestInfoTask;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback,
                                                        GoogleMap.OnMapClickListener,
                                                        GoogleMap.OnMyLocationButtonClickListener,
                                                        GoogleMap.OnMyLocationClickListener {

    private GoogleMap mMap;
    private static final int DEFAULT_ZOOM = 6;
    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng defaultLocation = new LatLng(39.694784, -8.130301);

    private MapFragmentListener mapFragmentListener;

    private List<PointOfInterest> pointOfInterestList;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private android.location.Location lastKnownLocation;
    private CameraPosition cameraPosition;

    // Keys for storing activity state.
    // [START maps_current_place_state_keys]
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    // [END maps_current_place_state_keys]

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     */
    private boolean locationPermission = false;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog. Save the return value, an instance of
    // ActivityResultLauncher, as an instance variable.
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    locationPermission = true;
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    locationPermission = false;
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

        // [START_EXCLUDE silent]
        // [START maps_current_place_on_create_save_instance_state]
        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        // [END maps_current_place_on_create_save_instance_state]
        // [END_EXCLUDE]

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);

        // Construct a FusedLocationProviderClient.
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
        for (int i = 0; i < pointOfInterestList.size(); i++)
            setPointOnMap(pointOfInterestList.get(i).getName(), pointOfInterestList.get(i).getLocation());
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
                .title(title);

        mMap.addMarker(marker);

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(poiLocation));
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermission) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                granted();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermission) {
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

    public void granted() {
        if(locationPermission)
            return;

        if (ContextCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.

            locationPermission = true;
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

    public interface MapFragmentListener {
        void mapActive(MapFragment mapFragment);
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

        TextView cancelButton = (TextView) dialog.findViewById(R.id.tv_permissions_cancel);
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        TextView okButton = (TextView) dialog.findViewById(R.id.tv_permissions_ok);
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

        TextView cancelButton = (TextView) dialog.findViewById(R.id.tv_permissions_cancel);
        cancelButton.setVisibility(View.GONE);

        TextView okButton = (TextView) dialog.findViewById(R.id.tv_permissions_ok);
        okButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}