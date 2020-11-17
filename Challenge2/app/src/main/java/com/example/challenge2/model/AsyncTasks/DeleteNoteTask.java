package com.example.challenge2.model.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.example.challenge2.R;
import com.example.challenge2.model.Repository.FileSystemManager;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.util.UUID;

/**
 * Classe DeleteNoteTask que representa a AsyncTask responsável por eliminar uma nota armazenada
 * em memória
 */
public class DeleteNoteTask extends AsyncTask<Void, Void, Void> {

    private final FragmentActivity activity;
    private final UUID noteUUID;
    private Exception exception;

    // Construtor da async task
    public DeleteNoteTask(FragmentActivity activity, UUID noteUUID) {
        this.activity = activity;
        this.noteUUID = noteUUID;
    }

    // Método executado no final da async task
    @Override
    protected void onPostExecute(Void arg) {
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
            FileSystemManager.removeNoteContent(activity, noteUUID);
        } catch (FileNotFoundException exception) {
            this.exception = exception;
        }
        return null;
    }
}
