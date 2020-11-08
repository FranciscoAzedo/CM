package com.example.challenge2.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.challenge2.R;
import com.example.challenge2.model.Note;

import java.util.ArrayList;

public class NoteListFragment extends Fragment {

    // View elements
    private TextView tvTotalNotes;
    private EditText etSearch;
    private ImageView ivAdd;

    // Recycler View elements
    private RecyclerView rvNotesList;
    private NoteListAdapter rvNotesListAdapter;
    private RecyclerView.LayoutManager rvNotesListLayoutManager;

    // Search secondary list
    private ArrayList<Note> notesSearchList;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes_list, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTotalNotes = view.findViewById(R.id.tv_total_notes);
        etSearch = view.findViewById(R.id.et_search);
        rvNotesList = view.findViewById(R.id.recycler_notes);
        ivAdd = view.findViewById(R.id.iv_add);

        // Atualizar o total de notas
        // [SUBSTITUIR] Forma como se vai buscar o total de notas
        tvTotalNotes.setText(getString(R.string.total_notes, String.format("%d", ((NoteActivity) getActivity()).notesList.size())));

        // Fazer o set da lista total na Search List
        // [SUBSTITUIR] Por abordagem melhor
        notesSearchList = new ArrayList<>(((NoteActivity) getActivity()).notesList);

        // Setup da Recycler View
        // [SUBSTITUIR] Como se está a ir buscar as notas
        rvNotesListLayoutManager = new LinearLayoutManager(getContext());
        rvNotesList.setLayoutManager(rvNotesListLayoutManager);
        rvNotesListAdapter = new NoteListAdapter(notesSearchList);
        rvNotesList.setAdapter(rvNotesListAdapter);

        // Listener para que quando haja um click mude para o Fragment de criar notas
        ivAdd.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_note, new NoteDetailedFragment())
                    .addToBackStack(getString(R.string.note_detailed_fragment_label))
                    .commit();
        });

        // Listener para pesquisar notas por título
        // [SUBSTITUIR] Fiz o método de filtro à pata portanto se alguém souber uma maneira mais eficiente
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Algoritmo para fazer filtragem na lista secundária
                for (Note note : ((NoteActivity) getActivity()).notesList) {

                    // Se o texto digitado for vazio e se a nota atual não estiver na lista secundária
                    if (s.length() == 0 && !notesSearchList.contains(note)) {
                        notesSearchList.add(note);
                        continue;
                    }

                    // Adicionar ou remover a nota atual à lista secundária se o título corresponder
                    if (note.getTitle().toLowerCase().contains(s.toString().toLowerCase())) {
                        if (!notesSearchList.contains(note))
                            notesSearchList.add(note);
                    } else {
                        notesSearchList.remove(note);
                    }
                }

                // Atualizar o Adapter
                rvNotesListAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}