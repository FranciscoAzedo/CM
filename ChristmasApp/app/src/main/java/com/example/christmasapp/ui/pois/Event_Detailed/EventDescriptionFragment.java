package com.example.christmasapp.ui.pois.Event_Detailed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.christmasapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventDescriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventDescriptionFragment extends Fragment {

   private TextView tvEventDescription;

    public EventDescriptionFragment() {
        // Required empty public constructor
    }


    public static EventDescriptionFragment newInstance() {
        EventDescriptionFragment fragment = new EventDescriptionFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_description, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewElements(view);
        populateView();

    }

    private void initViewElements(View view) {
        tvEventDescription = view.findViewById(R.id.event_description);
    }

    private void populateView() {
        tvEventDescription.setText("ASDASD ASD ASD ASD ASD ASD SAD ASD SAD ASD ASD " +
                "ASD ASD ASD ASD SDA ASD ASD D AS SAD ASD SAD ADS ASD  ASD ASD ASD ASD ASD " +
                " ASD ASD ASD ASD SD ASD AD AS ASDSD A SAD SAD SAD SAD SAD SAD ASD  ASD ASD ASD " +
                " ASD ASD ASD ADS ASD ASD D SA SAD ASD ASD ASD ASD ASD  DAS ASD ASD ASD ASD D AS" +
                "ASD ASD ASD ASD SDA ASD ASD D AS SAD ASD SAD ADS ASD  ASD ASD ASD ASD ASD " +
                " ASD ASD ASD ASD SD ASD AD AS ASDSD A SAD SAD SAD SAD SAD SAD ASD  ASD ASD ASD " +
                " ASD ASD ASD ADS ASD ASD D SA SAD ASD ASD ASD ASD ASD  DAS ASD ASD ASD ASD D AS " +
                "ASD ASD ASD ASD SDA ASD ASD D AS SAD ASD SAD ADS ASD  ASD ASD ASD ASD ASD " +
                " ASD ASD ASD ASD SD ASD AD AS ASDSD A SAD SAD SAD SAD SAD SAD ASD  ASD ASD ASD " +
                " ASD ASD ASD ADS ASD ASD D SA SAD ASD ASD ASD ASD ASD  DAS ASD ASD ASD ASD D AS " +
                "ASD ASD ASD ASD SDA ASD ASD D AS SAD ASD SAD ADS ASD  ASD ASD ASD ASD ASD " +
                " ASD ASD ASD ASD SD ASD AD AS ASDSD A SAD SAD SAD SAD SAD SAD ASD  ASD ASD ASD " +
                " ASD ASD ASD ADS ASD ASD D SA SAD ASD ASD ASD ASD ASD  DAS ASD ASD ASD ASD D AS " +
                "ASD ASD ASD ASD SDA ASD ASD D AS SAD ASD SAD ADS ASD  ASD ASD ASD ASD ASD " +
                " ASD ASD ASD ASD SD ASD AD AS ASDSD A SAD SAD SAD SAD SAD SAD ASD  ASD ASD ASD " +
                " ASD ASD ASD ADS ASD ASD D SA SAD ASD ASD ASD ASD ASD  DAS ASD ASD ASD ASD D AS " +
                "ASD ASD ASD ASD SDA ASD ASD D AS SAD ASD SAD ADS ASD  ASD ASD ASD ASD ASD " +
                " ASD ASD ASD ASD SD ASD AD AS ASDSD A SAD SAD SAD SAD SAD SAD ASD  ASD ASD ASD " +
                " ASD ASD ASD ADS ASD ASD D SA SAD ASD ASD ASD ASD ASD  DAS ASD ASD ASD ASD D AS " +
                "ASD ASD ASD ASD SDA ASD ASD D AS SAD ASD SAD ADS ASD  ASD ASD ASD ASD ASD " +
                " ASD ASD ASD ASD SD ASD AD AS ASDSD A SAD SAD SAD SAD SAD SAD ASD  ASD ASD ASD " +
                " ASD ASD ASD ADS ASD ASD D SA SAD ASD ASD ASD ASD ASD  DAS ASD ASD ASD ASD D AS ");
    }
}