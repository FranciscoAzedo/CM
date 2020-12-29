package com.example.christmasapp.ui.subscriptions;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import com.example.christmasapp.R;
import com.example.christmasapp.data.Utils;
import com.example.christmasapp.data.model.Topic;
import com.example.christmasapp.tasks.ReadTopicTask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionsFragment extends Fragment implements Serializable {

    private List<Topic> topicsList = new ArrayList<>();

    private SubscriptionsFragmentListener subscriptionsFragmentListener;

    private RecyclerView rvNotificationsList;
    private TextView tvNotificationsEmpty;
    private SubscriptionListAdapter rvSubscriptionsListAdapter;
    private LayoutManager rvNotificationsListLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subscriptions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewElements(view);
        populateView();
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Utils.ACTIVITY_KEY, (Serializable) getActivity());
        new ReadTopicTask(this, bundle).execute();
        subscriptionsFragmentListener.subscriptionsActive(this);
    }

    public void updateSubscriptions(List<Topic> topics) {
        this.topicsList.clear();
        this.topicsList.addAll(topics);
        getActivity().runOnUiThread(() -> {
            if (this.topicsList.isEmpty()) {
                rvNotificationsList.setVisibility(View.GONE);
                tvNotificationsEmpty.setVisibility(View.VISIBLE);
            } else {
                rvNotificationsList.setVisibility(View.VISIBLE);
                tvNotificationsEmpty.setVisibility(View.GONE);
            }
            rvSubscriptionsListAdapter.notifyDataSetChanged();
        });
    }

    private void initViewElements(View view) {
        tvNotificationsEmpty = view.findViewById(R.id.empty_subscriptions);
        rvNotificationsList = view.findViewById(R.id.recycler_subscriptions);
    }

    private void populateView() {
        rvNotificationsListLayoutManager = new LinearLayoutManager(getContext());
        rvNotificationsList.setLayoutManager(rvNotificationsListLayoutManager);
        rvSubscriptionsListAdapter = new SubscriptionListAdapter(topicsList, this);
        rvNotificationsList.setAdapter(rvSubscriptionsListAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SubscriptionsFragmentListener) {
            subscriptionsFragmentListener = (SubscriptionsFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNotesListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        subscriptionsFragmentListener = null;
    }

    public interface SubscriptionsFragmentListener {
        void subscriptionsActive(SubscriptionsFragment subscriptionsFragment);
    }
}