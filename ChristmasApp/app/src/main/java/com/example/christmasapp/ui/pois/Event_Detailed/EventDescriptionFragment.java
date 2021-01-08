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
import com.example.christmasapp.data.model.Event;
import com.example.christmasapp.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventDescriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventDescriptionFragment extends Fragment {

   private TextView tvEventDescription;

   private Event event;

    public EventDescriptionFragment() {
        // Required empty public constructor
    }


    public static EventDescriptionFragment newInstance() {
        EventDescriptionFragment fragment = new EventDescriptionFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initArguments();
    }

    private void initArguments() {
        if(getArguments() != null) {
            this.event = (Event) getArguments().getSerializable(Constants.POI_OBJECT_BUNDLE);
        }
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden == false) {
            initArguments();
            populateView();
        }
    }

    private void initViewElements(View view) {
        tvEventDescription = view.findViewById(R.id.event_description);
    }

    private void populateView() {
        tvEventDescription.setText(event.getDescription());
    }
}