package com.example.challenge2.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.challenge2.R;
import com.example.challenge2.model.Note;
import com.example.challenge2.model.Repository.SharedPreferencesManager;

import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity {

    // Only for test purposes
    // [SUBSTITUIR] Pelo que é dito no enunciado
    public ArrayList<Note> notesList = new ArrayList<Note>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        fillNotesList();
        // Apresentar inicialmente a lista de todas as notas
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_note, new NoteListFragment())
                .commit();
    }

    private void fillNotesList() {
        for (String s : SharedPreferencesManager.getSharedPreference(this, "titles")) {
            notesList.add(new Note(s, "texto"));
        }
    }
}