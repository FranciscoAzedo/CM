package com.example.challenge2.model.AsyncTasks;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.challenge2.R;
import com.example.challenge2.Utils;
import com.example.challenge2.view.NoteActivity;
import com.example.challenge2.view.NotificationManager;
import com.google.android.material.snackbar.Snackbar;

public class SaveNoteTask extends AsyncTask<Void, Void, Void> {

    private final FragmentActivity activity;
    private final Bundle bundle;
    private final NotificationManager notificationManager;
    private boolean result;

    public SaveNoteTask(FragmentActivity activity, Bundle bundle) {
        this.activity = activity;
        this.bundle = bundle;
        this.notificationManager = (NoteActivity) bundle.getSerializable(Utils.ACTIVITY_KEY);
    }

    @Override
    protected void onPostExecute(Void arg) {
        int message;
        if (result) {
            new PublishNoteTask(activity, bundle).execute();
            if (bundle.getString(Utils.OPERATION_KEY).equals(Utils.CREATE_NOTE_MODE)) {
                message = R.string.create_note_success;
                notificationManager.notifyNewNote(bundle);
            } else {
                message = R.string.update_note_success;
                notificationManager.notifyUpdateNote(bundle);
            }
        } else {
            if (bundle.getString(Utils.OPERATION_KEY).equals(Utils.CREATE_NOTE_MODE))
                message = R.string.create_note_error;
            else
                message = R.string.update_note_error;
        }

        Snackbar.make(activity.getWindow().getDecorView().findViewById(R.id.RelativeLayout),
                message,
                Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Void doInBackground(Void... args) {
        if (bundle.getString(Utils.OPERATION_KEY).equals(Utils.CREATE_NOTE_MODE))
            result = Utils.updateNotes(Utils.CREATE_NOTE_MODE, bundle);
        else
            result = Utils.updateNotes(Utils.CHANGE_NOTE_MODE, bundle);
        return null;
    }
}