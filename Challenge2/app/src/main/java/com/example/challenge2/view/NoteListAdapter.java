package com.example.challenge2.view;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.challenge2.R;

import java.util.ArrayList;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteListViewHolder> {

    private final ArrayList<String> notesList;
    private OnItemClickListener listener;
    private int positionLongPressed;

    public NoteListAdapter(ArrayList<String> notesList) {
        this.notesList = notesList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteListViewHolder holder, int position) {
        final String currentNote = notesList.get(position);

        holder.tvTitle.setText(currentNote);

        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(position);
        });

        holder.itemView.setOnLongClickListener(v -> {
            positionLongPressed = position;
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    @Override
    public void onViewRecycled(NoteListViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    public int getPositionLongPressed() {
        return positionLongPressed;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class NoteListViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private final TextView tvTitle;

        public NoteListViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_note_title);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, R.id.menu_change_title, Menu.NONE, R.string.long_press_change);
            menu.add(Menu.NONE, R.id.menu_delete, Menu.NONE, R.string.long_press_delete);
        }
    }
}
