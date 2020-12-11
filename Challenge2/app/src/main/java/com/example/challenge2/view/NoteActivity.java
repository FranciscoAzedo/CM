package com.example.challenge2.view;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.challenge2.MqttHelper;
import com.example.challenge2.R;
import com.example.challenge2.Utils;
import com.example.challenge2.model.AsyncTasks.ReadNotesTask;
import com.example.challenge2.model.AsyncTasks.ReadTopicsTask;
import com.example.challenge2.model.AsyncTasks.SaveNoteTask;
import com.example.challenge2.model.Repository.NoteKeeperDBHelper;
import com.example.challenge2.model.Topic;
import com.example.challenge2.view.fragment.ConnectionsFragment;
import com.example.challenge2.view.fragment.NoteDetailedFragment;
import com.example.challenge2.view.fragment.NoteListFragment;

import java.io.Serializable;
import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity
        implements NoteListFragment.OnNotesListFragmentInteractionListener,
        NoteDetailedFragment.OnNoteDetailsFragmentInteractionListener,
        ConnectionsFragment.OnConnectionsFragmentInteractionListener,
        NotificationManager,
        Serializable {

    private NoteKeeperDBHelper noteKeeperDBHelper;
    private MqttHelper mqttHelper;
    private NoteListFragment noteListFragment;
    private NoteDetailedFragment noteDetailedFragment;
    private ConnectionsFragment connectionsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        noteKeeperDBHelper = new NoteKeeperDBHelper(this);
        mqttHelper = new MqttHelper(noteKeeperDBHelper, this);

        Bundle fragmentBundle = new Bundle();
        fragmentBundle.putSerializable(Utils.DATABASE_HELPER_KEY, noteKeeperDBHelper);
        noteListFragment = NoteListFragment.newInstance();
        noteListFragment.setArguments(fragmentBundle);

        // Iniciar fragmento com lista das notas
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_note, noteListFragment)
                .addToBackStack(null)
                .commit();

        startReadNotesTask();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mqttHelper.mqttAndroidClient.unregisterResources();
        mqttHelper.mqttAndroidClient.close();
    }

    @Override
    public void OnNoteDetailsFragmentInteraction(Bundle bundle) {

        if ((noteListFragment = (NoteListFragment) getSupportFragmentManager().findFragmentByTag("noteListFragment")) == null)
            noteListFragment = NoteListFragment.newInstance();

        bundle.putSerializable(Utils.DATABASE_HELPER_KEY, noteKeeperDBHelper);
        bundle.putSerializable(Utils.MQTT_HELPER_KEY, mqttHelper);
        noteListFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_note, noteListFragment, "noteListFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void OnNotesListFragmentInteraction(Bundle bundle) {
        if ((noteDetailedFragment = (NoteDetailedFragment) getSupportFragmentManager().findFragmentByTag("noteDetailsFragment")) == null)
            noteDetailedFragment = NoteDetailedFragment.newInstance();

        noteDetailedFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_note, noteDetailedFragment, "noteDetailsFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void OnConnectionsFragmentInteraction(Bundle bundle) {
        if ((connectionsFragment = (ConnectionsFragment) getSupportFragmentManager().findFragmentByTag("connectionsFragment")) == null)
            connectionsFragment = ConnectionsFragment.newInstance();

        bundle.putSerializable(Utils.ACTIVITY_KEY, this);
        connectionsFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_note, connectionsFragment, "connectionsFragment")
                .addToBackStack(null)
                .commit();

        startReadTopicsTask(false);
    }

    @Override
    public void OnConnectionsBackFragmentInteraction(Bundle bundle) {
        if ((noteListFragment = (NoteListFragment) getSupportFragmentManager().findFragmentByTag("noteListFragment")) == null)
            noteListFragment = NoteListFragment.newInstance();

        bundle.putSerializable(Utils.DATABASE_HELPER_KEY, noteKeeperDBHelper);
        noteListFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_note, noteListFragment, "noteListFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void notifyConnection(Bundle bundle) {
        if (bundle.getBoolean(Utils.CONNECTION_STATUS_KEY))
            bundle.putSerializable(Utils.MQTT_HELPER_KEY, mqttHelper);
        startReadTopicsTask(true);
        noteListFragment.notifyConnection(bundle);
    }

    @Override
    public void notifyLoadedNotes(Bundle bundle) {
        noteListFragment.notifyLoadedNotes(bundle);
    }

    @Override
    public void notifyNewNote(Bundle bundle) {
        noteListFragment.notifyNewNote(bundle);
    }

    @Override
    public void notifyUpdateNote(Bundle bundle) {
        noteListFragment.notifyUpdateNote(bundle);
    }

    @Override
    public void notifyDeletedNote(Bundle bundle) {
        noteListFragment.notifyDeletedNote(bundle);
    }

    @Override
    public void notifySubscription(Bundle bundle) {
        String title = bundle.getString(Utils.NOTE_TITLE_KEY);
        new AlertDialog.Builder(this)
                .setTitle(R.string.subscription_dialog_title)
                .setMessage(getString(R.string.subscription_dialog_content, title))
                .setPositiveButton(R.string.delete_dialog_confirm, (dialog, which) -> {
                    bundle.putSerializable(Utils.DATABASE_HELPER_KEY, noteKeeperDBHelper);
                    bundle.putSerializable(Utils.MQTT_HELPER_KEY, mqttHelper);
                    bundle.putSerializable(Utils.ACTIVITY_KEY, this);
                    new SaveNoteTask(this, bundle).execute();
                }).setNegativeButton(R.string.delete_dialog_cancel, (dialog, which) -> {
            dialog.dismiss();
        }).show();
    }

    @Override
    public void notifyLoadedTopics(Bundle bundle) {
        boolean connection = bundle.getBoolean(Utils.CONNECTION_KEY);
        if (connection)
            subscribeTopics(bundle);
        else
            connectionsFragment.notifyLoadedTopics(bundle);
    }

    @Override
    public void notifyNewTopic(Bundle bundle) {
        connectionsFragment.notifyNewTopic(bundle);
    }

    @Override
    public void notifyDeletedTopic(Bundle bundle) {
        connectionsFragment.notifyDeletedTopic(bundle);
    }

    private void startReadNotesTask() {
        Bundle readNotesBundle = new Bundle();
        readNotesBundle.putSerializable(Utils.DATABASE_HELPER_KEY, noteKeeperDBHelper);
        readNotesBundle.putSerializable(Utils.ACTIVITY_KEY, this);
        new ReadNotesTask(readNotesBundle).execute();
    }

    private void startReadTopicsTask(boolean connection) {
        Bundle readTopicsBundle = new Bundle();
        readTopicsBundle.putBoolean(Utils.CONNECTION_KEY, connection);
        readTopicsBundle.putSerializable(Utils.DATABASE_HELPER_KEY, noteKeeperDBHelper);
        readTopicsBundle.putSerializable(Utils.ACTIVITY_KEY, this);
        new ReadTopicsTask(readTopicsBundle).execute();
    }

    private void subscribeTopics(Bundle bundle) {
//        mqttHelper.subscribeToTopic(Utils.PUBLISH_TOPIC_KEY);
        ArrayList<Topic> topicList = (ArrayList<Topic>) bundle.getSerializable(Utils.LIST_TOPICS_KEY);
        if (topicList != null)
            for (Topic topic : topicList)
                mqttHelper.subscribeToTopic(topic.getName());
    }
}