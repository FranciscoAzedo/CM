package com.example.challenge2.view.fragment;

import android.content.Context;
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
import com.example.challenge2.model.AsyncTasks.ReadNoteTask;
import com.example.challenge2.model.Repository.SharedPreferencesManager;
import com.example.challenge2.view.NoteListAdapter;

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

    // Existing notes list
    private ArrayList<String> notesList;

    // Search secondary list
    private ArrayList<String> notesSearchList;

    // Fragment listener
    private OnNotesListFragmentInteractionListener mListener;

    public static NoteListFragment newInstance() {
        return new NoteListFragment();
    }

    private void initArguments() {
        // Inicializar as notas gaurdadas pela aplicação
        notesList = new ArrayList<>(SharedPreferencesManager.getSharedPreference(getActivity(), "titles"));
        notesSearchList = new ArrayList<>(notesList);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Initialize arguments
        initArguments();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes_list, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTotalNotes = view.findViewById(R.id.tv_total_notes);
        etSearch = view.findViewById(R.id.et_search);
        rvNotesList = view.findViewById(R.id.recycler_notes);
        ivAdd = view.findViewById(R.id.iv_add);

        // Indicar o número de notas mostradas
        tvTotalNotes.setText(getString(R.string.total_notes, String.format("%d", notesList.size())));

        // Setup da Recycler View
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
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Limpar a lista de pesquisa
                notesSearchList.clear();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Caso em que a pesquisa está vazia
                if (s.length() == 0)
                    notesSearchList.addAll(notesList);

                    // Caso em que é inserda alguma pesquisa
                else
                    for (String noteTittle : notesList)
                        if (noteTittle.equalsIgnoreCase(s.toString()) || noteTittle.contains(s))
                            notesSearchList.add(noteTittle);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Atualizar o numero de notas na pesquisa
                tvTotalNotes.setText(getString(R.string.total_notes, String.valueOf(notesSearchList.size())));
                // Atualizar o Adapter
                rvNotesListAdapter.notifyDataSetChanged();
            }
        });

        // Listener para quando houver um clique, ou longo clique, num elemento da lista de notas
        rvNotesListAdapter.setOnItemClickListener(new NoteListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("edit", true);
                bundle.putString("title", notesList.get(position));
                bundle.putString("content", "");

                new ReadNoteTask(getActivity(), notesList.get(position), bundle).execute();
                mListener.OnNotesListFragmentInteraction(bundle);
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
    public void editNote(int index) {
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

                    if (!newTitle.isEmpty()) {

                        Bundle bundle = new Bundle();
                        bundle.putString("title", notesSearchList.get(index));

                        // Gerar título com ID
//                        String uuidNoteTitle = etNoteTitle.getText().toString() + "-" + Utils.generateUUID();

                        // [SUBSTITUIR] Falta o código agora para alterar na nota
                        notesSearchList.set(index, newTitle);
                        notesSearchList.set(index, newTitle);

                        // Atualizar o Adapter
                        rvNotesListAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.change_dialog_cancel, (dialog, whichButton) -> {
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNotesListFragmentInteractionListener) {
            mListener = (OnNotesListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNotesListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnNotesListFragmentInteractionListener {
        void OnNotesListFragmentInteraction(Bundle bundle);
    }
}
