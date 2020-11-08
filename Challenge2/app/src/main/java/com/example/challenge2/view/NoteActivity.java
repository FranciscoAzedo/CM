package com.example.challenge2.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.challenge2.R;
import com.example.challenge2.model.Note;

import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity {

    // Only for test purposes
    // [SUBSTITUIR] Pelo que é dito no enunciado
    public ArrayList<Note> notesList = new ArrayList<Note>() {
        {
            add(new Note("Test 1", "Text of Test 1"));
            add(new Note("Test 2", "Text of Test 2"));
            add(new Note("Test 3", "Text of Test 3"));
            add(new Note("Test 4", "Text of Test 4"));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        // Apresentar inicialmente a lista de todas as notas
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_note, new NoteListFragment())
                .addToBackStack(getString(R.string.note_list_fragment_label))
                .commit();
    }
}