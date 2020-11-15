package com.example.challenge2.model.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.example.challenge2.R;
import com.example.challenge2.model.NoteContent;
import com.example.challenge2.model.Repository.FileSystemManager;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;

public class SaveNoteTask extends AsyncTask<Void, Void, Void> {

    private Exception exception;
    private Boolean update;
    private FragmentActivity activity;
    private NoteContent noteContent;

    // Construtor da async task
    public SaveNoteTask(FragmentActivity activity, NoteContent noteContent, Boolean update) {
        this.activity = activity;
        this.noteContent = noteContent;
        this.update = update;
    }

    // Método executado no final da async task
    @Override
    protected void onPostExecute(Void arg) {
        if (exception != null) {
            Log.e("EXCEPTION", "Exception " + exception + "has occurred!");
            // Criar e mostrar snackbar de erro
            Snackbar sbError = Snackbar.make(activity.getWindow().getDecorView().findViewById(R.id.RelativeLayout),
                    R.string.save_note_error,
                    Snackbar.LENGTH_SHORT);
            sbError.show();
        }
    }

    // Método executado quando a async task é chamada
    @Override
    protected Void doInBackground(Void... args) {
        try {
            if (update)
                FileSystemManager.updateNoteContent(activity, noteContent);
            else
                FileSystemManager.saveNoteContent(activity, noteContent);
        } catch (FileNotFoundException exception) {
            this.exception = exception;
        }
        return null;
    }
}