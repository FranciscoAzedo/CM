package com.example.christmasapp.ui.pois.Event_Detailed;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.christmasapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventInfoFragment extends Fragment {

    private TextView tvPrice;
    private TextView tvSchedule;
    private ImageView ivLocation;

    public EventInfoFragment() {
        // Required empty public constructor
    }

    public static EventInfoFragment newInstance() {
        EventInfoFragment fragment = new EventInfoFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewElements(view);
        populateView();
    }

    private void initViewElements(View view) {
        /* References to View elements */
        tvPrice = view.findViewById(R.id.price);
        tvSchedule = view.findViewById(R.id.schedule);
        ivLocation = view.findViewById(R.id.location);
    }

    private void populateView() {
        tvPrice.setText("De borla para todos!");
        tvSchedule.setText("É dia e noite! Isto é dia e noite!");
        ivLocation.setBackgroundColor(Color.CYAN);
    }
}