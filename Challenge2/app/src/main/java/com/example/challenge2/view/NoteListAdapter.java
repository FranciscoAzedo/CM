package com.example.challenge2.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.challenge2.R;
import com.example.challenge2.model.Note;

import java.util.ArrayList;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteListViewHolder> {

    private final ArrayList<Note> notesList;

    public NoteListAdapter(ArrayList<Note> notesList) {
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public NoteListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteListViewHolder holder, int position) {
        final Note currentNote = notesList.get(position);

        holder.tvTitle.setText(currentNote.getTitle());
        //holder.tvTitle.setText(currentNote.getDate());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public static class NoteListViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;
        //private TextView tvDate;

        public NoteListViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_note_title);
            //tvDate = itemView.findViewById(R.id.tv_note_date);
        }
    }
}
