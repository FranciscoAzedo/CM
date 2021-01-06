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

import java.sql.Date;
import java.sql.Time;
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

    public EventScheduleFragment() {
        // Required empty public constructor
    }

    public static EventScheduleFragment newInstance() {
        EventScheduleFragment fragment = new EventScheduleFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        agendaInstanceList.add(new AgendaInstance("Teste", new Time(20, 0, 0), new Time(20, 20, 20), new Date(2021, 1, 1)));
        return inflater.inflate(R.layout.fragment_event_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewElements(view);
        populateView();
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