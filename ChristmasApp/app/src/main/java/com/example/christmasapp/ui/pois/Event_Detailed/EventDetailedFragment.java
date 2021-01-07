package com.example.christmasapp.ui.pois.Event_Detailed;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.christmasapp.R;
import com.example.christmasapp.data.model.Event;
import com.example.christmasapp.ui.subscriptions.SubscriptionListAdapter;
import com.example.christmasapp.utils.Constants;
import com.example.christmasapp.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventDetailedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventDetailedFragment extends Fragment {

    private ImageView ivPOIImage;
    private TextView tvPOIName;
    private TextView tvPOILocation;
    private TextView tvPOIEventDescription;
    private TextView tvPOIEventSchedule;
    private TextView tvPOIEventInfo;
    private ImageView ivPOIEventDescription;
    private ImageView ivPOIEventSchedule;
    private ImageView ivPOIEventInfo;

    private EventDescriptionFragment eventDescriptionFragment;
    private EventInfoFragment eventInfoFragment;
    private EventScheduleFragment eventScheduleFragment;

    private Event event;


    public EventDetailedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventDetailedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventDetailedFragment newInstance(String param1, String param2) {
        EventDetailedFragment fragment = new EventDetailedFragment();
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
        return inflater.inflate(R.layout.fragment_event_detailed, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden == false) {
            initArguments();
            updateView();
        }
    }

    private void updateView() {
        tvPOIName.setText(event.getName());
        tvPOILocation.setText(event.getLocation().getName());
        ivPOIImage.setImageBitmap(event.getBitmap());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewElements(view);
        populateView();
    }

    private void initViewElements(View view) {
        ivPOIImage = view.findViewById(R.id.poi_image);
        tvPOIName = view.findViewById(R.id.poi_name);
        tvPOILocation = view.findViewById(R.id.poi_location);
        tvPOIEventDescription = view.findViewById(R.id.event_description);
        tvPOIEventSchedule = view.findViewById(R.id.event_schedule);
        tvPOIEventInfo = view.findViewById(R.id.event_info);
        ivPOIEventDescription  = view.findViewById(R.id.selected_description);
        ivPOIEventSchedule = view.findViewById(R.id.selected_schedule);
        ivPOIEventInfo = view.findViewById(R.id.selected_info);

        tvPOIEventDescription.setOnClickListener(v -> {
            ivPOIEventDescription.setVisibility(View.VISIBLE);
            ivPOIEventSchedule.setVisibility(View.INVISIBLE);
            ivPOIEventInfo.setVisibility(View.INVISIBLE);

            if ((eventDescriptionFragment = (EventDescriptionFragment) getChildFragmentManager().findFragmentByTag("eventDescriptionFragment")) == null)
                eventDescriptionFragment = EventDescriptionFragment.newInstance();

            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.POI_OBJECT_BUNDLE, event);
            eventDescriptionFragment.setArguments(bundle);

            getChildFragmentManager().beginTransaction()
                    .replace(R.id.event_detailed_selected_fragment, eventDescriptionFragment, "eventDescriptionFragment")
                    .addToBackStack(null)
                    .commit();

        });

        tvPOIEventSchedule.setOnClickListener(v -> {
            ivPOIEventDescription.setVisibility(View.INVISIBLE);
            ivPOIEventSchedule.setVisibility(View.VISIBLE);
            ivPOIEventInfo.setVisibility(View.INVISIBLE);

            if ((eventScheduleFragment = (EventScheduleFragment) getChildFragmentManager().findFragmentByTag("eventScheduleFragment")) == null)
                eventScheduleFragment = EventScheduleFragment.newInstance();

            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.POI_OBJECT_BUNDLE, event);
            eventScheduleFragment.setArguments(bundle);

            getChildFragmentManager().beginTransaction()
                    .replace(R.id.event_detailed_selected_fragment, eventScheduleFragment, "eventScheduleFragment")
                    .addToBackStack(null)
                    .commit();
        });

        tvPOIEventInfo.setOnClickListener(v -> {
            ivPOIEventDescription.setVisibility(View.INVISIBLE);
            ivPOIEventSchedule.setVisibility(View.INVISIBLE);
            ivPOIEventInfo.setVisibility(View.VISIBLE);

            if ((eventInfoFragment = (EventInfoFragment) getChildFragmentManager().findFragmentByTag("eventInfoFragment")) == null)
                eventInfoFragment = EventInfoFragment.newInstance();

            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.POI_OBJECT_BUNDLE, event);
            eventInfoFragment.setArguments(bundle);

            getChildFragmentManager().beginTransaction()
                    .replace(R.id.event_detailed_selected_fragment, eventInfoFragment, "eventInfoFragment")
                    .addToBackStack(null)
                    .commit();
        });

    }

    private void populateView() {
        ivPOIEventDescription.setVisibility(View.VISIBLE);
        ivPOIEventSchedule.setVisibility(View.INVISIBLE);
        ivPOIEventInfo.setVisibility(View.INVISIBLE);

        tvPOIName.setText(event.getName());
        tvPOILocation.setText(event.getLocation().getName());
        ivPOIImage.setImageBitmap(event.getBitmap());

        eventDescriptionFragment = EventDescriptionFragment.newInstance();

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.POI_OBJECT_BUNDLE, event);
        eventDescriptionFragment.setArguments(bundle);

        getChildFragmentManager().beginTransaction()
                .replace(R.id.event_detailed_selected_fragment, eventDescriptionFragment, "eventDescriptionFragment")
                .addToBackStack(null)
                .commit();
    }
}