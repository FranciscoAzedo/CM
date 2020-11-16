package com.example.challenge2.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.example.challenge2.Utils;
import com.example.challenge2.model.AsyncTasks.ReadNoteTask;
import com.example.challenge2.model.NoteContent;
import com.example.challenge2.model.Repository.SharedPreferencesManager;
import com.example.challenge2.view.NoteListAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
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
    private ArrayList<String> noteTitlesList;

    // Search secondary list
    private ArrayList<String> noteTitlesSearchList;

    // Fragment listener
    private OnNotesListFragmentInteractionListener mListener;

    public static NoteListFragment newInstance() {
        return new NoteListFragment();
    }

    private void initArguments() {
        // Inicializar as notas gaurdadas pela aplicação
        noteTitlesList = new ArrayList<>(SharedPreferencesManager.getSharedPreference(getActivity(), "titles"));
        noteTitlesSearchList = new ArrayList<>(noteTitlesList);
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

        tvTotalNotes.setText(getString(R.string.total_notes, String.valueOf(noteTitlesSearchList.size())));

        // Setup da Recycler View
        rvNotesListLayoutManager = new LinearLayoutManager(getContext());
        rvNotesList.setLayoutManager(rvNotesListLayoutManager);
        rvNotesListAdapter = new NoteListAdapter(noteTitlesSearchList);
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
                noteTitlesSearchList.clear();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Caso em que a pesquisa está vazia
                if (s.length() == 0)
                    noteTitlesSearchList.addAll(noteTitlesList);

                    // Caso em que é inserda alguma pesquisa
                else
                    for (String noteTittle : noteTitlesList)
                        if (Utils.getNoteTitle(noteTittle).equalsIgnoreCase(s.toString())
                                || Utils.getNoteTitle(noteTittle).contains(s))
                            noteTitlesSearchList.add(noteTittle);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Atualizar o numero de notas na pesquisa
                tvTotalNotes.setText(getString(R.string.total_notes, String.valueOf(noteTitlesSearchList.size())));
                // Atualizar o Adapter
                rvNotesListAdapter.notifyDataSetChanged();
            }
        });

        // Listener para quando houver um clique, ou longo clique, num elemento da lista de notas
        rvNotesListAdapter.setOnItemClickListener(new NoteListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int index) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Utils.EDIT_MODE_KEY, true);
                bundle.putString(Utils.NOTE_TITLE_KEY, noteTitlesSearchList.get(index));
                bundle.putSerializable(Utils.NOTE_CONTENT_KEY, new NoteContent(null, null));

                new ReadNoteTask(getActivity(), Utils.getUUIDFromTitle(noteTitlesSearchList.get(index)), bundle).execute();
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
        int index = -1;

        try {
            index = rvNotesListAdapter.getPositionLongPressed();
        } catch (Exception e) {
            return super.onContextItemSelected(item);
        }

        switch (item.getItemId()) {
            case R.id.menu_change_title:
                editNote(index);
                break;
            case R.id.menu_delete:
                deleteNote(index);
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

                    try {
                        // Obter o novo título digitado
                        String newTitle = edittext.getText().toString()
                                + Utils.SPLIT_STRING_PATTERN
                                + Utils.getUUIDFromTitle(noteTitlesSearchList.get(index));

                        if (!newTitle.isEmpty()) {

                            Bundle bundle = new Bundle();
                            bundle.putString(Utils.NOTE_TITLE_KEY, newTitle);
                            Utils.updateNotes(Utils.CHANGE_NOTE_TITLE_MODE, getActivity(), bundle);

                            // [SUBSTITUIR] Falta o código agora para alterar na nota
                            updateNoteTitle(newTitle, index);
                        }
                    } catch (FileNotFoundException exception) {
                        notifyException(exception);
                    } finally {
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
    public void deleteNote(int index) {
        // Apresentar a dialog para o utilizador apagar ou não
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.delete_dialog_title)
                .setMessage(R.string.delete_dialog_content)
                .setPositiveButton(R.string.delete_dialog_confirm, (dialog, which) -> {
                    try {
                        // Apagar a nota
                        Bundle bundle = new Bundle();
                        bundle.putString(Utils.NOTE_TITLE_KEY, noteTitlesSearchList.get(index));
                        bundle.putString(Utils.NOTE_UUID_KEY, String.valueOf(Utils.getUUIDFromTitle(noteTitlesSearchList.get(index))));
                        Utils.updateNotes(Utils.DELETE_NOTE_MODE, getActivity(), bundle);

                        // Remover nota da lista
                        noteTitlesList.remove(noteTitlesSearchList.remove(index));
                        tvTotalNotes.setText(getString(R.string.total_notes, String.valueOf(noteTitlesSearchList.size())));

                        // Atualizar o Adapter
                        rvNotesListAdapter.notifyDataSetChanged();
                    } catch (FileNotFoundException exception) {
                        Log.e("EXCEPTION", "Exception " + exception + "has occurred!");
                        Snackbar.make(getActivity().getWindow().getDecorView().findViewById(R.id.RelativeLayout),
                                R.string.read_note_error,
                                Snackbar.LENGTH_SHORT)
                                .show();
                    }
                }).setNegativeButton(R.string.delete_dialog_cancel, (dialog, which) -> {
            dialog.dismiss();
        }).show();
    }

    private void notifyException(Exception exception) {
        Log.e("EXCEPTION", "Exception " + exception + "has occurred!");
        Snackbar.make(getActivity().getWindow().getDecorView().findViewById(R.id.RelativeLayout),
                R.string.read_note_error,
                Snackbar.LENGTH_SHORT)
                .show();
    }

    private void updateNoteTitle(String noteTitle, int index) {
        for (String currentNoteTitle : noteTitlesList)
            if (currentNoteTitle.contains(String.valueOf(Utils.getUUIDFromTitle(noteTitle)))) {
                noteTitlesList.remove(currentNoteTitle);
                noteTitlesList.add(noteTitle);
                break;
            }
        if (Utils.getNoteTitle(noteTitle).contains(etSearch.getText().toString()))
            noteTitlesSearchList.set(index, noteTitle);
        else {
            noteTitlesSearchList.remove(index);
            // Atualizar o numero de notas na pesquisa
            tvTotalNotes.setText(getString(R.string.total_notes, String.valueOf(noteTitlesSearchList.size())));
        }
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
