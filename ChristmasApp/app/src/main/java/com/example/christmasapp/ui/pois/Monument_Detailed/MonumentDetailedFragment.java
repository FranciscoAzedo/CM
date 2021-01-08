package com.example.christmasapp.ui.pois.Monument_Detailed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.christmasapp.R;
import com.example.christmasapp.data.model.PointOfInterest;
import com.example.christmasapp.data.model.Topic;
import com.example.christmasapp.tasks.DeleteTopicTask;
import com.example.christmasapp.tasks.ReadPointOfInterestImageTask;
import com.example.christmasapp.tasks.SaveTopicTask;
import com.example.christmasapp.utils.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class MonumentDetailedFragment extends Fragment {


    // TODO: Rename and change types of parameters
    private PointOfInterest pointOfInterest;

    private TextView tvPoIName;
    private ImageView ivPoIIcon;
    private TextView tvPoILocationName;
    private TextView tvPoIDescription;
    private ImageView ivPoIThumb;
    private MapView mMapView;

    private GoogleMap googleMap;

    public MonumentDetailedFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        return new MonumentDetailedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initArguments();
    }

    private void initArguments() {
        if(getArguments() != null) {
            this.pointOfInterest = (PointOfInterest) getArguments().getSerializable(Constants.POI_OBJECT_BUNDLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_monument_detailed, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.poi_map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(mMap -> {
            googleMap = mMap;

            // Block map UI from user
            mMap.getUiSettings().setAllGesturesEnabled(false);
            mMap.setOnMapClickListener(null);

            // For dropping a marker at a point on the Map
            LatLng eventMarker = new LatLng(pointOfInterest.getLocation().getLatitude(), pointOfInterest.getLocation().getLongitude());
            googleMap.addMarker(new MarkerOptions().position(eventMarker));

            // For zooming automatically to the location of the marker
            CameraPosition cameraPosition = new CameraPosition.Builder().target(eventMarker).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewElements(view);
        populateView();
        checkIfImageExists();
    }

    @Override
    public void onResume() {
        super.onResume();
        initArguments();
        populateView();
        checkIfImageExists();
    }

    private void checkIfImageExists() {
        if(pointOfInterest != null && pointOfInterest.getBitmap() == null) {
            new ReadPointOfInterestImageTask(this, pointOfInterest).execute();
        }
    }

    private void initViewElements(View view) {
        ivPoIThumb = view.findViewById(R.id.poi_image);
        tvPoIName = view.findViewById(R.id.poi_name);
        tvPoILocationName = view.findViewById(R.id.poi_location);
        tvPoIDescription = view.findViewById(R.id.poi_description);
        ivPoIIcon = view.findViewById(R.id.poi_icon);
//        tvPoIDescription.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);

        ivPoIIcon.setOnClickListener(v -> {
            Topic topic = new Topic(
                    pointOfInterest.getName(),
                    pointOfInterest.getImageUrl()
            );

            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.TOPIC_KEY, topic);
            bundle.putSerializable(Constants.ACTIVITY_KEY, (Serializable) getActivity());

            if(pointOfInterest.isSubscribed()) {
                new DeleteTopicTask(bundle).execute();
                ivPoIIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_empty));
            } else {
                bundle.putBoolean(Constants.MQTT_SUBSCRIBED, false);
                new SaveTopicTask(bundle).execute();
                ivPoIIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_filled));
            }
            pointOfInterest.setSubscribed(!pointOfInterest.isSubscribed());
        });
    }

    private void populateView() {
        if(pointOfInterest != null) {
            ivPoIThumb.setImageBitmap(pointOfInterest.getBitmap());
            tvPoIName.setText(pointOfInterest.getName());
            tvPoILocationName.setText(pointOfInterest.getLocation().getName());
            tvPoIDescription.setText(pointOfInterest.getDescription());
            if (pointOfInterest.isSubscribed())
                ivPoIIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_filled));
            else
                ivPoIIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_empty));
        }
    }

    public void updatePointOfInterestImages(@NotNull PointOfInterest pointOfInterest) {
        this.pointOfInterest.setBitmap(pointOfInterest.getBitmap());
        ivPoIThumb.setImageBitmap(this.pointOfInterest.getBitmap());
    }
}