package com.example.challenge2.model.AsyncTasks;

import android.os.AsyncTask;
import android.os.Bundle;

import com.example.challenge2.Utils;
import com.example.challenge2.model.Note;
import com.example.challenge2.model.Repository.NoteKeeperDBHelper;
import com.example.challenge2.view.NoteActivity;
import com.example.challenge2.view.NotificationManager;

import java.io.Serializable;
import java.util.List;

/**
 * Classe ReadNoteTask que representa a AsyncTask responsável por ler uma nota armazenada em memória
 */
public class ReadNotesTask extends AsyncTask<Void, Void, Void> {

    private final NoteKeeperDBHelper noteKeeperDBHelper;
    private final NotificationManager notificationManager;

    private List<Note> notes;

    // Construtor da async task
    public ReadNotesTask(Bundle bundle) {
        this.noteKeeperDBHelper = (NoteKeeperDBHelper) bundle.getSerializable(Utils.DATABASE_HELPER_KEY);
        this.notificationManager = (NoteActivity) bundle.getSerializable(Utils.ACTIVITY_KEY);
    }

    // Método executado quando a async task é chamada
    @Override
    protected Void doInBackground(Void... args) {
        notes = noteKeeperDBHelper.getAllNotes();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Utils.NOTE_LIST_KEY, (Serializable) notes);
        notificationManager.notifyLoadedNotes(bundle);
        return null;
    }
}
