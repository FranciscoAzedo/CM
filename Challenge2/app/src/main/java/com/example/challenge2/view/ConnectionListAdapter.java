package com.example.challenge2.view;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.challenge2.R;
import com.example.challenge2.Utils;
import com.example.challenge2.model.AsyncTasks.DeleteTopicTask;
import com.example.challenge2.model.Topic;

import java.util.ArrayList;

public class ConnectionListAdapter extends RecyclerView.Adapter<ConnectionListAdapter.ConnectionListViewHolder> {

    private final ArrayList<Topic> topicsList;
    private final FragmentActivity activity;
    private final Bundle bundle;

    private AlertDialog alertDialog;

    public ConnectionListAdapter(ArrayList<Topic> topicsList, FragmentActivity activity, Bundle bundle) {
        this.activity = activity;
        this.topicsList = topicsList;
        this.bundle = bundle;
    }

    @NonNull
    @Override
    public ConnectionListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_connection, parent, false);
        return new ConnectionListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ConnectionListViewHolder holder, int index) {

        holder.tvTitle.setText(topicsList.get(index).getName());

        holder.ivDelete.setOnClickListener(v -> {
            alertDialog = popConfirmationNoteDelete(index);
            alertDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return topicsList.size();
    }

    @Override
    public void onViewRecycled(ConnectionListViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    private AlertDialog popConfirmationNoteDelete(int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.delete_dialog_topic_title)
                .setMessage(R.string.delete_dialog_topic_content)
                .setPositiveButton(R.string.delete_dialog_topic_confirm, (dialog, id) -> {
                    bundle.putSerializable(Utils.TOPIC_KEY, topicsList.get(index));
                    new DeleteTopicTask(activity, bundle).execute();
                })
                .setNegativeButton(R.string.delete_dialog_topic_cancel, null);
        return builder.create();
    }

    public static class ConnectionListViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private final TextView tvTitle;
        private final ImageView ivDelete;

        public ConnectionListViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_note_title);
            ivDelete = itemView.findViewById(R.id.iv_delete);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

        }
    }
}
