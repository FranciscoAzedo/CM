package com.example.christmasapp.ui.subscriptions;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.christmasapp.R;
import com.example.christmasapp.utils.Constants;
import com.example.christmasapp.tasks.DeleteTopicTask;

import java.io.Serializable;
import java.util.List;

public class SubscriptionListAdapter extends RecyclerView.Adapter<SubscriptionListAdapter.SubscriptionListViewHolder> {

    private final List<String> topicsList;
    private final SubscriptionsFragment subscriptionsFragment;

    public SubscriptionListAdapter(List<String> topicsList, SubscriptionsFragment subscriptionsFragment) {
        this.topicsList = topicsList;
        this.subscriptionsFragment = subscriptionsFragment;
    }

    @NonNull
    @Override
    public SubscriptionListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subscription, parent, false);
        return new SubscriptionListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionListViewHolder holder, int index) {
        holder.tvTitle.setText(topicsList.get(index));

        holder.ivDelete.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.TOPIC_KEY, topicsList.get(index));
            bundle.putSerializable(Constants.SUBSCRIPTION_FRAGMENT_KEY, subscriptionsFragment);
            bundle.putSerializable(Constants.ACTIVITY_KEY, (Serializable) subscriptionsFragment.getActivity());
            new DeleteTopicTask(bundle).execute();
        });
    }

    @Override
    public int getItemCount() {
        return topicsList.size();
    }

    @Override
    public void onViewRecycled(SubscriptionListViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    public static class SubscriptionListViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private final TextView tvTitle;
        private final TextView tvDescription;
        private final ImageView ivRead;
        private final ImageView ivDelete;
        private final View viewListener;

        public SubscriptionListViewHolder(@NonNull View itemView) {
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
