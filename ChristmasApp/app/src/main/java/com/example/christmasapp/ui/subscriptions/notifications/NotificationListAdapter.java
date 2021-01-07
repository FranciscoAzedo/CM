package com.example.christmasapp.ui.subscriptions.notifications;

import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import com.example.christmasapp.R;
import com.example.christmasapp.utils.Constants;
import com.example.christmasapp.data.model.Notification;
import com.example.christmasapp.tasks.DeleteNotificationTask;
import com.example.christmasapp.tasks.UpdateNotificationTask;
import com.example.christmasapp.utils.Utils;

import java.io.Serializable;
import java.util.List;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.NotificationListViewHolder> {

    private final List<Notification> notificationList;
    private final NotificationsFragment notificationsFragment;
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public NotificationListAdapter(List<Notification> notificationList, NotificationsFragment notificationsFragment) {
        this.notificationList = notificationList;
        this.notificationsFragment = notificationsFragment;
    }

    @NonNull
    @Override
    public NotificationListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationListViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull NotificationListViewHolder holder, int index) {

        if (notificationList.get(index).getRead()) {
            holder.ivNewNotification.setVisibility(View.INVISIBLE);
            holder.ivRead.setVisibility(View.GONE);
            holder.ivDelete.setVisibility(View.VISIBLE);
        } else {
            holder.ivNewNotification.setVisibility(View.VISIBLE);
            holder.ivRead.setVisibility(View.VISIBLE);
            holder.ivDelete.setVisibility(View.GONE);
        }

        holder.tvTitle.setText(notificationList.get(index).getTitle());
        holder.tvDescription.setText(notificationList.get(index).getDescription());
        holder.tvTime.setText(Utils.getNotificationTime(notificationList.get(index)));

        holder.ivRead.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.NOTIFICATION_KEY, notificationList.get(index));
            bundle.putSerializable(Constants.NOTIFICATION_FRAGMENT_KEY, notificationsFragment);
            bundle.putSerializable(Constants.ACTIVITY_KEY, (Serializable) notificationsFragment.getActivity());
            new UpdateNotificationTask(bundle).execute();
        });

        holder.ivDelete.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.NOTIFICATION_KEY, notificationList.get(index));
            bundle.putSerializable(Constants.NOTIFICATION_FRAGMENT_KEY, notificationsFragment);
            bundle.putSerializable(Constants.ACTIVITY_KEY, (Serializable) notificationsFragment.getActivity());
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

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class NotificationListViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private final TextView tvTitle;
        private final TextView tvDescription;
        private final TextView tvTime;
        private final ImageView ivRead;
        private final ImageView ivDelete;
        private final ImageView ivNewNotification;
        private final View viewListener;

        public NotificationListViewHolder(@NonNull View itemView) {
            super(itemView);
            viewListener = itemView;
            tvTitle = itemView.findViewById(R.id.tv_notification_title);
            tvDescription = itemView.findViewById(R.id.tv_notification_description);
            tvTime = itemView.findViewById(R.id.tv_notification_time);
            ivRead = itemView.findViewById(R.id.iv_read);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            ivNewNotification = itemView.findViewById(R.id.iv_new_notification);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

        }
    }
}
