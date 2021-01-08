package com.example.christmasapp.ui.subscriptions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import com.example.christmasapp.R;
import com.example.christmasapp.data.model.Event;
import com.example.christmasapp.data.model.PointOfInterest;
import com.example.christmasapp.data.model.Topic;
import com.example.christmasapp.helpers.SharedPreferencesHelper;
import com.example.christmasapp.tasks.ReadPointOfInterestInfoTask;
import com.example.christmasapp.utils.Constants;
import com.example.christmasapp.tasks.ReadTopicTask;
import com.example.christmasapp.utils.Utils;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionsFragment extends Fragment implements Serializable {

    private List<Topic> topicsList = new ArrayList<>();

    private SubscriptionsFragmentListener subscriptionsFragmentListener;

    private RecyclerView rvSubscriptionsList;
    private TextView tvSubscriptionsEmpty;
    private TextView tvSubscriptionsCount;
    private TextView tvNotificationsCount;
    private SubscriptionListAdapter rvSubscriptionsListAdapter;
    private LayoutManager rvSubscriptionsListLayoutManager;
    private Bundle initialBundle;

    private List<PointOfInterest> pointOfInterestList;

    private int notificationCounter = 0;

    // Flag indicating connectivity status
    private boolean isNetworkAvailable = false;

    /* View elements */
    private ImageView ivNotificationsBell;

    // Broadcast Receiver to handles connectivity changes (enabled/disabled)
    private final BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();

                // Update connectivity flag according to the current connectivity status
                if(!TextUtils.isEmpty(action) && action.matches("android.net.conn.CONNECTIVITY_CHANGE")) {
                    isNetworkAvailable = Utils.isOnline(getContext());

                    // Get the list of points of interest asynchronously
                    if (isNetworkAvailable && pointOfInterestList.isEmpty() && topicsList.isEmpty()) {
                        new ReadPointOfInterestInfoTask(SubscriptionsFragment.this).execute();
                        new ReadTopicTask(SubscriptionsFragment.this, initialBundle).execute();
                    }
                }
            }
        }
    };

    public static SubscriptionsFragment newInstance() {
        return new SubscriptionsFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subscriptions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewElements(view);
        populateView();

        // Get the list of points of interest asynchronously
        if(isNetworkAvailable)
            new ReadPointOfInterestInfoTask(this).execute();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Register broadcast receiver to be aware of changes on connectivity status
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        requireContext().registerReceiver(networkReceiver, intentFilter);

        initialBundle = new Bundle();
        initialBundle.putSerializable(Constants.ACTIVITY_KEY, (Serializable) getActivity());
        if(isNetworkAvailable)
            new ReadTopicTask(this, initialBundle).execute();
        subscriptionsFragmentListener.subscriptionsActive(this);
    }

    /**
     * Unregisters broadcast receivers
     */
    @Override
    public void onPause() {
        // Unregister the broadcast receiver
        requireContext().unregisterReceiver(networkReceiver);

        super.onPause();
    }

    public void updateSubscriptions(List<Topic> topics) {
        this.topicsList.clear();
        this.topicsList.addAll(topics);
        try {
            getActivity().runOnUiThread(() -> {
                if (this.topicsList.isEmpty()) {
                    rvSubscriptionsList.setVisibility(View.GONE);
                    tvSubscriptionsCount.setVisibility(View.GONE);
                    tvSubscriptionsEmpty.setVisibility(View.VISIBLE);
                } else {
                    rvSubscriptionsList.setVisibility(View.VISIBLE);
                    tvSubscriptionsCount.setVisibility(View.VISIBLE);
                    tvSubscriptionsCount.setText(topicsList.size() + " subscrição(ões)");
                    tvSubscriptionsEmpty.setVisibility(View.GONE);
                }
                rvSubscriptionsListAdapter.notifyDataSetChanged();
            });
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

    public void updatePointsOfInterest(List<PointOfInterest> pointOfInterestList){
        this.pointOfInterestList.addAll(pointOfInterestList);
    }

    private void initViewElements(View view) {
        /* References to View elements */
        tvSubscriptionsEmpty = view.findViewById(R.id.empty_subscriptions);
        tvSubscriptionsCount = view.findViewById(R.id.count_subscriptions);
        rvSubscriptionsList = view.findViewById(R.id.recycler_subscriptions);
        ivNotificationsBell = view.findViewById(R.id.iv_notifications_bell);
        tvNotificationsCount = view.findViewById(R.id.tv_notifications_bell_counter);

        /* Listener to click on the notification's bell */
        ivNotificationsBell.setOnClickListener(v -> {
//            Navigation.findNavController(getView()).navigate(R.id.action_subscriptions_to_notifications);
            subscriptionsFragmentListener.notificationsActive(this);
            notificationCounter = 0;
            SharedPreferencesHelper.getInstance(getActivity()).setNotifications(Constants.NOTIFICATIONS_KEY,0);
            tvNotificationsCount.setVisibility(View.INVISIBLE);
        });
        this.pointOfInterestList = new ArrayList<>();
    }

    private void populateView() {
        rvSubscriptionsListLayoutManager = new LinearLayoutManager(getContext());
        rvSubscriptionsList.setLayoutManager(rvSubscriptionsListLayoutManager);
        rvSubscriptionsListAdapter = new SubscriptionListAdapter(topicsList, this);
        rvSubscriptionsList.setAdapter(rvSubscriptionsListAdapter);

        rvSubscriptionsListAdapter.setOnItemClickListener(index -> {
            PointOfInterest pointOfInterest = getPointOfInterestByName(topicsList.get(index).getName());
            if(pointOfInterest != null) {
                if (pointOfInterest instanceof Event)
                    subscriptionsFragmentListener.subscriptionToEventDetails(this, pointOfInterest);
                else
                    subscriptionsFragmentListener.subscriptionToMonumentDetails(this, pointOfInterest);
            }
            else {
                Snackbar
                        .make(getView(), "Ocorreu um erro durante o processo", BaseTransientBottomBar.LENGTH_SHORT)
                        .show();
            }
        });

        notificationCounter = SharedPreferencesHelper.getInstance(getActivity()).getNotifications(Constants.NOTIFICATIONS_KEY);

        if (notificationCounter > 0)
            tvNotificationsCount.setText(String.valueOf(notificationCounter));
        else {
            tvNotificationsCount.setVisibility(View.INVISIBLE);
            rvSubscriptionsList.setVisibility(View.INVISIBLE);
        }
    }

    private PointOfInterest getPointOfInterestByName(String name){
        for (PointOfInterest pointOfInterest : pointOfInterestList)
            if (pointOfInterest.getName().equalsIgnoreCase(name))
                return pointOfInterest;
        return null;
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

    public void addNotification() {
        notificationCounter++;
        SharedPreferencesHelper.getInstance(getActivity()).setNotifications(Constants.NOTIFICATIONS_KEY,notificationCounter);
        tvNotificationsCount.setVisibility(View.VISIBLE);
        if (notificationCounter < 10)
            tvNotificationsCount.setText(String.valueOf(notificationCounter));
        else
            tvNotificationsCount.setText("9+");
    }

    public interface SubscriptionsFragmentListener {
        void subscriptionsActive(SubscriptionsFragment subscriptionsFragment);
        void notificationsActive(SubscriptionsFragment subscriptionsFragment);
        void subscriptionToEventDetails(SubscriptionsFragment subscriptionsFragment, PointOfInterest pointOfInterest);
        void subscriptionToMonumentDetails(SubscriptionsFragment subscriptionsFragment, PointOfInterest pointOfInterest);
    }
}