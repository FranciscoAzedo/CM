package com.example.christmasapp.ui.pois.Event_Detailed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import com.example.christmasapp.R;
import com.example.christmasapp.data.model.AgendaInstance;
import com.example.christmasapp.data.model.Event;
import com.example.christmasapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventScheduleFragment extends Fragment {

    private List<AgendaInstance> agendaInstanceList = new ArrayList<>();

    private RecyclerView rvAgendaInstancesList;
    private EventSchedulesListAdapter rvAgendaInstancesListAdapter;
    private LayoutManager rvAgendaInstancesListLayoutManager;

    private Event event;

    public EventScheduleFragment() {
        // Required empty public constructor
    }

    public static EventScheduleFragment newInstance() {
        EventScheduleFragment fragment = new EventScheduleFragment();
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
            agendaInstanceList.addAll(event.getAgenda());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_schedule, container, false);
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
            rvAgendaInstancesListAdapter.notifyDataSetChanged();
        }
    }

    private void initViewElements(View view) {
        /* References to View elements */
        rvAgendaInstancesList = view.findViewById(R.id.recycler_event_schedules);
    }

    private void populateView() {
        rvAgendaInstancesListLayoutManager = new LinearLayoutManager(getContext());
        rvAgendaInstancesList.setLayoutManager(rvAgendaInstancesListLayoutManager);
        rvAgendaInstancesListAdapter = new EventSchedulesListAdapter(agendaInstanceList);
        rvAgendaInstancesList.setAdapter(rvAgendaInstancesListAdapter);
    }
}