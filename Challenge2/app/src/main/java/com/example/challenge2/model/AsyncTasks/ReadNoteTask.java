package com.example.challenge2.model.AsyncTasks;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.example.challenge2.R;
import com.example.challenge2.model.NoteContent;
import com.example.challenge2.model.Repository.FileSystemManager;
import com.example.challenge2.view.fragment.NoteDetailedFragment;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

public class ReadNoteTask extends AsyncTask<Void, Void, Void> {

    private Exception exception;
    private FragmentActivity activity;
    private UUID noteUUID;
    private NoteContent noteContent;
    private Bundle bundle;

    // Construtor da async task
    public ReadNoteTask(FragmentActivity activity, UUID noteUUID, Bundle bundle) {
        this.activity = activity;
        this.noteUUID = noteUUID;
        this.bundle = bundle;
    }

    // Método executado no final da async task
    @Override
    protected void onPostExecute(Void arg) {

        NoteDetailedFragment noteDetailedFragment;

        if ((noteDetailedFragment = (NoteDetailedFragment) activity.getSupportFragmentManager().findFragmentByTag("noteDetailsFragment")) != null)
            noteDetailedFragment.updateView();

        if (exception != null) {
            Log.e("EXCEPTION", "Exception " + exception + "has occurred!");
            // Criar e mostrar snackbar de erro
            Snackbar.make(activity.getWindow().getDecorView().findViewById(R.id.RelativeLayout),
                    R.string.read_note_error,
                    Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    // Método executado quando a async task é chamada
    @Override
    protected Void doInBackground(Void... args) {
        try {
            noteContent = FileSystemManager.readNoteContent(activity, noteUUID);
            bundle.putSerializable("noteContent", noteContent);
        } catch (FileNotFoundException exception) {
            this.exception = exception;
        }
        return null;
    }
}
