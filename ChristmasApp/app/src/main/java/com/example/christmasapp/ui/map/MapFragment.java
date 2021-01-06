package com.example.christmasapp.ui.map;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.christmasapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment {

    private MapFragmentListener mapFragmentListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

        supportMapFragment.getMapAsync(googleMap -> googleMap.setOnMapClickListener(latLng -> {
            //When clicked on map
            //Initialize marker options
            MarkerOptions markerOptions = new MarkerOptions();
            //Set position of mark
            markerOptions.position(latLng);
            markerOptions.title(latLng.latitude + " : " + latLng.longitude);
            //Remove all marker
            googleMap.clear();

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    latLng, 10
            ));
            googleMap.addMarker(markerOptions);
        }));
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

    public interface MapFragmentListener {
        void mapActive(MapFragment mapFragment);
    }
}