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
    private OnItemClickListener listener;

    public NoteListAdapter(ArrayList<Note> notesList) {
        this.notesList = notesList;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
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
        final Note currentNote = notesList.get(position);

        holder.tvTitle.setText(currentNote.getTitle());
        //holder.tvTitle.setText(currentNote.getDate());

        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(position);
        });

        holder.itemView.setOnLongClickListener(v -> {
            listener.onItemLongClick(position);
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public interface OnItemClickListener{
        void onItemClick(int position);

        void onItemLongClick(int position);
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
