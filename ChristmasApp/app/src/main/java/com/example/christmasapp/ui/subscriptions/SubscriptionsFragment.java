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
import com.example.christmasapp.data.model.Topic;
import com.example.christmasapp.utils.Constants;
import com.example.christmasapp.tasks.ReadTopicTask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SubscriptionsFragment extends Fragment implements Serializable {

    private List<Topic> topicsList = new ArrayList<>();

    private SubscriptionsFragmentListener subscriptionsFragmentListener;

    private RecyclerView rvSubscriptionsList;
    private TextView tvSubscriptionsEmpty;
    private TextView tvSubscriptionsCount;
    private SubscriptionListAdapter rvSubscriptionsListAdapter;
    private LayoutManager rvSubscriptionsListLayoutManager;

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
        bundle.putSerializable(Constants.ACTIVITY_KEY, (Serializable) getActivity());
        new ReadTopicTask(this, bundle).execute();
        subscriptionsFragmentListener.subscriptionsActive(this);
    }

    public void updateSubscriptions(Set<Topic> topics) {
        this.topicsList.clear();
        this.topicsList.addAll(topics);
        getActivity().runOnUiThread(() -> {
            if (this.topicsList.isEmpty()) {
                rvSubscriptionsList.setVisibility(View.GONE);
                tvSubscriptionsCount.setVisibility(View.GONE);
                tvSubscriptionsEmpty.setVisibility(View.VISIBLE);
            } else {
                rvSubscriptionsList.setVisibility(View.VISIBLE);
                tvSubscriptionsCount.setVisibility(View.VISIBLE);
                tvSubscriptionsCount.setText(topicsList.size() + " subscription(s)");
                tvSubscriptionsEmpty.setVisibility(View.GONE);
            }
            rvSubscriptionsListAdapter.notifyDataSetChanged();
        });
    }

    private void initViewElements(View view) {
        tvSubscriptionsEmpty = view.findViewById(R.id.empty_subscriptions);
        tvSubscriptionsCount = view.findViewById(R.id.count_subscriptions);
        rvSubscriptionsList = view.findViewById(R.id.recycler_subscriptions);
    }

    private void populateView() {
        rvSubscriptionsListLayoutManager = new LinearLayoutManager(getContext());
        rvSubscriptionsList.setLayoutManager(rvSubscriptionsListLayoutManager);
        rvSubscriptionsListAdapter = new SubscriptionListAdapter(topicsList, this);
        rvSubscriptionsList.setAdapter(rvSubscriptionsListAdapter);
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