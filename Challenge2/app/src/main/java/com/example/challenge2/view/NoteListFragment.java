package com.example.challenge2.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

        // Listener para quando houver um clique, ou longo clique, num elemento da lista de notas
        rvNotesListAdapter.setOnItemClickListener(new NoteListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Abrir a nota respetiva
                // [SUBSTITUIR] Enviar a nota que é clicada
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_note, new NoteDetailedFragment())
                        .addToBackStack(getString(R.string.note_detailed_fragment_label))
                        .commit();
            }
        });
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_long_press, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = -1;

        try {
            position = rvNotesListAdapter.getPositionLongPressed();
        } catch (Exception e) {
            return super.onContextItemSelected(item);
        }

        switch (item.getItemId()) {
            case R.id.menu_change_title:
                editNote(position);
                break;
            case R.id.menu_delete:
                deleteNote();
                break;
        }

        return super.onContextItemSelected(item);
    }

    /**
     * Método que lida com a intenção do utilizador mudar o título de uma nota
     */
    public void editNote(int position) {
        // Criar a caixa de Texto para o utilizador inserir o novo título
        // Podia ser feito com recurso a uma Custom View este Dialog
        final EditText edittext = new EditText(getContext());
        edittext.setHint(R.string.change_dialog_edit_hint);

        // Apresentar a dialog para o utilizador apagar ou não
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.change_dialog_title)
                .setView(edittext)
                .setPositiveButton(R.string.change_dialog_confirm, (dialog, whichButton) -> {
                    // Obter o novo título digitado
                    String newTitle = edittext.getText().toString();

                    if (!newTitle.isEmpty())
                        // [SUBSTITUIR] Falta o código agora para alterar na nota
                        notesSearchList.get(position).setTitle(newTitle);

                    // Atualizar o Adapter
                    rvNotesListAdapter.notifyDataSetChanged();
                }).show();
    }

    /**
     * Método que lida com a intenção do utilizador eliminar uma nota
     */
    public void deleteNote() {
        // Apresentar a dialog para o utilizador apagar ou não
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.delete_dialog_title)
                .setMessage(R.string.delete_dialog_content)
                .setPositiveButton(R.string.delete_dialog_confirm, (dialog, which) -> {
                    // Apagar a nota

                    // Atualizar o Adapter
                    rvNotesListAdapter.notifyDataSetChanged();

                }).setNegativeButton(R.string.delete_dialog_cancel, (dialog, which) -> {
            dialog.dismiss();
        }).show();
    }
}