package com.example.christmasapp.ui.subscriptions;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.christmasapp.R;
import com.example.christmasapp.data.Utils;
import com.example.christmasapp.data.model.Notification;
import com.example.christmasapp.tasks.DeleteNotificationTask;
import com.example.christmasapp.tasks.ReadNotificationTask;
import com.example.christmasapp.tasks.UpdateNotificationTask;

import java.io.Serializable;
import java.util.List;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.NotificationListViewHolder> {

    private final List<Notification> notificationList;
    private final SubscriptionsFragment subscriptionsFragment;

    public NotificationListAdapter(List<Notification> notificationList, SubscriptionsFragment subscriptionsFragment) {
        this.notificationList = notificationList;
        this.subscriptionsFragment = subscriptionsFragment;
    }

    @NonNull
    @Override
    public NotificationListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationListViewHolder holder, int index) {

        if (notificationList.get(index).getRead()) {
            holder.viewListener.setBackgroundColor(Color.parseColor("#DADADA"));
            holder.tvTitle.setAlpha(0.6f);
            holder.tvDescription.setAlpha(0.6f);
            holder.ivRead.setVisibility(View.GONE);
        } else {
            holder.viewListener.setBackgroundColor(Color.WHITE);
            holder.tvTitle.setAlpha(1);
            holder.tvDescription.setAlpha(1);
            holder.ivRead.setVisibility(View.VISIBLE);
        }


        holder.tvTitle.setText(notificationList.get(index).getTitle());
        holder.tvDescription.setText(notificationList.get(index).getDescription());

        holder.ivRead.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Utils.NOTIFICATION_KEY, notificationList.get(index));
            bundle.putSerializable(Utils.SUBSCRIPTION_FRAGMENT_KEY, subscriptionsFragment);
            bundle.putSerializable(Utils.ACTIVITY_KEY, (Serializable) subscriptionsFragment.getActivity());
            new UpdateNotificationTask(bundle).execute();
        });

        holder.ivDelete.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Utils.NOTIFICATION_KEY, notificationList.get(index));
            bundle.putSerializable(Utils.SUBSCRIPTION_FRAGMENT_KEY, subscriptionsFragment);
            bundle.putSerializable(Utils.ACTIVITY_KEY, (Serializable) subscriptionsFragment.getActivity());
            new DeleteNotificationTask(bundle).execute();
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    @Override
    public void onViewRecycled(NotificationListViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    public static class NotificationListViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private final TextView tvTitle;
        private final TextView tvDescription;
        private final ImageView ivRead;
        private final ImageView ivDelete;
        private final View viewListener;

        public NotificationListViewHolder(@NonNull View itemView) {
            super(itemView);
            viewListener = itemView;
            tvTitle = itemView.findViewById(R.id.tv_notification_title);
            tvDescription = itemView.findViewById(R.id.tv_notification_description);
            ivRead = itemView.findViewById(R.id.iv_read);
            ivDelete = itemView.findViewById(R.id.iv_delete);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

        }
    }
}
