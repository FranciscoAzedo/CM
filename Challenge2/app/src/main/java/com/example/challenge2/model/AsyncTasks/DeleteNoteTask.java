package com.example.challenge2.model.AsyncTasks;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.challenge2.R;
import com.example.challenge2.Utils;
import com.example.challenge2.view.NoteActivity;
import com.example.challenge2.view.NotificationManager;
import com.google.android.material.snackbar.Snackbar;

public class DeleteNoteTask extends AsyncTask<Void, Void, Void> {

    private final FragmentActivity activity;
    private final Bundle bundle;
    private final NotificationManager notificationManager;
    private boolean result;

    public DeleteNoteTask(FragmentActivity activity, Bundle bundle) {
        this.activity = activity;
        this.bundle = bundle;
        this.notificationManager = (NoteActivity) bundle.getSerializable(Utils.ACTIVITY_KEY);
    }

    @Override
    protected void onPostExecute(Void arg) {
        int message;

        if (result) {
            message = R.string.delete_note_success;
            notificationManager.notifyDeletedNote(bundle);
        } else {
            message = R.string.delete_note_error;
        }

        Snackbar.make(activity.getWindow().getDecorView().findViewById(R.id.RelativeLayout),
                message,
                Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Void doInBackground(Void... args) {
        result = Utils.updateNotes(Utils.DELETE_NOTE_MODE, bundle);
        return null;
    }
}
