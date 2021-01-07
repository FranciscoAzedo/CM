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
import com.example.christmasapp.utils.Constants;

public class MonumentDetailedFragment extends Fragment {


    // TODO: Rename and change types of parameters
    private PointOfInterest pointOfInterest;

    private TextView tvPoIName;
    private TextView tvPoILocationName;
    private TextView tvPoIDescription;
    private ImageView ivPoIThumb;

    public MonumentDetailedFragment() {
        // Required empty public constructor
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
        return inflater.inflate(R.layout.fragment_monument_detailed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewElements(view);
        populateView();
    }

    private void initViewElements(View view) {
        ivPoIThumb = view.findViewById(R.id.poi_image);
        tvPoIName = view.findViewById(R.id.poi_name);
        tvPoILocationName = view.findViewById(R.id.poi_location);
        tvPoIDescription = view.findViewById(R.id.poi_description);
    }

    private void populateView() {
        if(pointOfInterest != null) {
            ivPoIThumb.setImageBitmap(pointOfInterest.getBitmap());
            tvPoIName.setText(pointOfInterest.getName());
            tvPoILocationName.setText(pointOfInterest.getLocation().getName());
            tvPoIDescription.setText(pointOfInterest.getDescription());
        }
    }
}