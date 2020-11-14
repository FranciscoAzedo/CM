package com.example.challenge2.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.challenge2.R;
import com.example.challenge2.view.fragment.NoteDetailedFragment;
import com.example.challenge2.view.fragment.NoteListFragment;

public class NoteActivity extends AppCompatActivity
        implements NoteListFragment.OnNotesListFragmentInteractionListener,
        NoteDetailedFragment.OnNoteDetailsFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        // Iniciar fragmento com lista das notas
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_note, NoteListFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void OnNoteDetailsFragmentInteraction(Bundle bundle) {
        NoteListFragment noteListFragment;

        if ((noteListFragment = (NoteListFragment) getSupportFragmentManager().findFragmentByTag("noteListFragment")) == null)
            noteListFragment = NoteListFragment.newInstance();

        noteListFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_note, noteListFragment, "noteListFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void OnNotesListFragmentInteraction(Bundle bundle) {
        NoteDetailedFragment noteDetailedFragment;

        if ((noteDetailedFragment = (NoteDetailedFragment) getSupportFragmentManager().findFragmentByTag("noteDetailsFragment")) == null)
            noteDetailedFragment = NoteDetailedFragment.newInstance();

        noteDetailedFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_note, noteDetailedFragment, "noteDetailsFragment")
                .addToBackStack(null)
                .commit();
    }
}