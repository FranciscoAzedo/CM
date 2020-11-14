package com.example.challenge2.model.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.example.challenge2.R;
import com.example.challenge2.model.Repository.FileSystemManager;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;

public class SaveNoteTask extends AsyncTask<Void, Void, Void> {

    private Exception exception;
    private FragmentActivity activity;
    private String title;
    private String content;

    // Construtor da async task
    public SaveNoteTask(FragmentActivity activity, String title, String content) {
        this.activity = activity;
        this.title = title;
        this.content = content;
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
            FileSystemManager.createNoteFile(activity, title, content);
        } catch (FileNotFoundException exception) {
            this.exception = exception;
        }
        return null;
    }
}