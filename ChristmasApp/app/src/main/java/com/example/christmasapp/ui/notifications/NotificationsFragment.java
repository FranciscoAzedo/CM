package com.example.christmasapp.ui.notifications;

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
import com.example.christmasapp.data.model.Notification;
import com.example.christmasapp.tasks.ReadNotificationTask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment implements Serializable {

    private List<Notification> notificationsList = new ArrayList<>();

    private NotificationFragmentListener notificationFragmentListener;

    private RecyclerView rvNotificationsList;
    private TextView tvNotificationsEmpty;
    private TextView tvNotificationsCount;
    private NotificationListAdapter rvNotificationsListAdapter;
    private LayoutManager rvNotificationsListLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
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
        new ReadNotificationTask(this).execute();
        notificationFragmentListener.notificationsActive(this);
    }

    public void updateNotifications(List<Notification> notifications) {
        this.notificationsList.clear();
        this.notificationsList.addAll(notifications);
        getActivity().runOnUiThread(() -> {
            if (this.notificationsList.isEmpty()) {
                rvNotificationsList.setVisibility(View.GONE);
                tvNotificationsCount.setVisibility(View.GONE);
                tvNotificationsEmpty.setVisibility(View.VISIBLE);
            } else {
                rvNotificationsList.setVisibility(View.VISIBLE);
                tvNotificationsCount.setVisibility(View.VISIBLE);
                tvNotificationsCount.setText(notifications.size() + " notification(s)");
                tvNotificationsEmpty.setVisibility(View.GONE);
            }
            rvNotificationsListAdapter.notifyDataSetChanged();
        });
    }

    private void initViewElements(View view) {
        tvNotificationsEmpty = view.findViewById(R.id.empty_notifications);
        tvNotificationsCount = view.findViewById(R.id.count_notifications);
        rvNotificationsList = view.findViewById(R.id.recycler_notifications);
    }

    private void populateView() {
        rvNotificationsListLayoutManager = new LinearLayoutManager(getContext());
        rvNotificationsList.setLayoutManager(rvNotificationsListLayoutManager);
        rvNotificationsListAdapter = new NotificationListAdapter(notificationsList, this);
        rvNotificationsList.setAdapter(rvNotificationsListAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NotificationFragmentListener) {
            notificationFragmentListener = (NotificationFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNotesListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        notificationFragmentListener = null;
    }

    public interface NotificationFragmentListener {
        void notificationsActive(NotificationsFragment notificationsFragment);
    }
}