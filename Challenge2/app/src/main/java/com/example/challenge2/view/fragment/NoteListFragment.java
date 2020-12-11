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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.challenge2.MqttHelper;
import com.example.challenge2.R;
import com.example.challenge2.Utils;
import com.example.challenge2.model.AsyncTasks.DeleteNoteTask;
import com.example.challenge2.model.AsyncTasks.SaveNoteTask;
import com.example.challenge2.model.Note;
import com.example.challenge2.model.Repository.NoteKeeperDBHelper;
import com.example.challenge2.view.NoteActivity;
import com.example.challenge2.view.NoteListAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.ArrayList;

public class NoteListFragment extends Fragment {

    // View elements
    private TextView tvTotalNotes;
    private EditText etSearch;
    private ImageView ivAdd;
    private Button btnConnections;


    // Recycler View elements
    private RecyclerView rvNotesList;
    private NoteListAdapter rvNotesListAdapter;
    private RecyclerView.LayoutManager rvNotesListLayoutManager;

    //  notes lists
    private ArrayList<Note> noteTitlesList = new ArrayList<>();
    private ArrayList<Note> noteTitlesSearchList = new ArrayList<>();

    // Fragment listener
    private OnNotesListFragmentInteractionListener mListener;

    private NoteKeeperDBHelper noteKeeperDBHelper;
    private Boolean connection = false;
    private MqttHelper mqttHelper;

    public static NoteListFragment newInstance() {
        return new NoteListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes_list, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initArguments();
        initViewElements(view);
        populateView();

        // Listener para que quando haja um click mude para o Fragment de criar notas
        ivAdd.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Utils.EDIT_MODE_KEY, false);
            bundle.putSerializable(Utils.DATABASE_HELPER_KEY, noteKeeperDBHelper);
            bundle.putSerializable(Utils.MQTT_HELPER_KEY, mqttHelper);
            bundle.putSerializable(Utils.LIST_NOTES_KEY, noteTitlesList);
            bundle.putSerializable(Utils.LIST_SEARCH_NOTES_KEY, noteTitlesSearchList);
            mListener.OnNotesListFragmentInteraction(bundle);
        });

        btnConnections.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Utils.DATABASE_HELPER_KEY, noteKeeperDBHelper);
            bundle.putSerializable(Utils.LIST_NOTES_KEY, noteTitlesList);
            bundle.putSerializable(Utils.LIST_SEARCH_NOTES_KEY, noteTitlesSearchList);
            mListener.OnConnectionsFragmentInteraction(bundle);
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

                    // Caso em que é inserida alguma pesquisa
                else
                    for (Note note : noteTitlesList)
                        if (note.getTitle().equalsIgnoreCase(s.toString())
                                || note.getTitle().contains(s))
                            noteTitlesSearchList.add(note);
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
        rvNotesListAdapter.setOnItemClickListener(index -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Utils.EDIT_MODE_KEY, true);
            bundle.putSerializable(Utils.DATABASE_HELPER_KEY, noteKeeperDBHelper);
            bundle.putSerializable(Utils.MQTT_HELPER_KEY, mqttHelper);
            bundle.putSerializable(Utils.NOTE_KEY, noteTitlesSearchList.get(index));
            bundle.putSerializable(Utils.LIST_NOTES_KEY, noteTitlesList);
            bundle.putSerializable(Utils.LIST_SEARCH_NOTES_KEY, noteTitlesSearchList);
            mListener.OnNotesListFragmentInteraction(bundle);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        rvNotesListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_long_press, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int index;

        // Obter o índex da nota sob o qual o utilizador fez long press
        try {
            index = rvNotesListAdapter.getPositionLongPressed();
        } catch (Exception e) {
            return super.onContextItemSelected(item);
        }

        // Obter a operação desejada pelo utilizador
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

    public void notifyConnection(Bundle bundle) {
        int message;
        connection = bundle.getBoolean(Utils.CONNECTION_STATUS_KEY);
        if (connection) {
            mqttHelper = (MqttHelper) bundle.getSerializable(Utils.MQTT_HELPER_KEY);
            message = R.string.connection_topic_success;
        } else
            message = R.string.connection_topic_error;

        Snackbar.make(getActivity().getWindow().getDecorView().findViewById(R.id.RelativeLayout),
                message,
                Snackbar.LENGTH_SHORT)
                .show();
    }

    public void notifyLoadedNotes(Bundle bundle) {
        if (bundle != null) {
            noteTitlesList = (ArrayList<Note>) bundle.getSerializable(Utils.NOTE_LIST_KEY);
            noteTitlesSearchList = new ArrayList<>(noteTitlesList);
        } else {
            noteTitlesList = new ArrayList<>();
            noteTitlesSearchList = new ArrayList<>();
        }
    }

    public void notifyNewNote(Bundle bundle) {
        if (bundle != null) {
            Note note = (Note) bundle.getSerializable(Utils.NOTE_KEY);
            noteTitlesList.add(note);

            if (note.getTitle().contains(etSearch.getText().toString()))
                noteTitlesSearchList.add(note);

            tvTotalNotes.setText(getString(R.string.total_notes, String.valueOf(noteTitlesSearchList.size())));
            rvNotesListAdapter.notifyDataSetChanged();
        }
    }

    public void notifyDeletedNote(Bundle bundle) {

        Note note = (Note) bundle.getSerializable(Utils.NOTE_KEY);

        noteTitlesSearchList.remove(note);
        noteTitlesList.remove(note);

        tvTotalNotes.setText(getString(R.string.total_notes, String.valueOf(noteTitlesSearchList.size())));
        rvNotesListAdapter.notifyDataSetChanged();
    }

    public void notifyUpdateNote(Bundle bundle) {

        Note note = (Note) bundle.getSerializable(Utils.NOTE_KEY);
        String title = bundle.getString(Utils.NOTE_TITLE_KEY);
        String content = bundle.getString(Utils.NOTE_CONTENT_KEY);

        int noteTitlesListIndex = noteTitlesList.indexOf(note);
        int noteTitlesSearchListIndex = noteTitlesSearchList.indexOf(note);

        if (title != null)
            note.setTitle(title);

        if (content != null)
            note.setContent(content);

        noteTitlesSearchList.set(noteTitlesSearchListIndex, note);
        noteTitlesList.set(noteTitlesListIndex, note);

        if (!note.getTitle().contains(etSearch.getText().toString()))
            noteTitlesSearchList.remove(note);

        tvTotalNotes.setText(getString(R.string.total_notes, String.valueOf(noteTitlesSearchList.size())));
        rvNotesListAdapter.notifyDataSetChanged();
    }

    private void initArguments() {
        if (getArguments() != null) {
            noteKeeperDBHelper = (NoteKeeperDBHelper) getArguments().getSerializable(Utils.DATABASE_HELPER_KEY);
            mqttHelper = (MqttHelper) getArguments().getSerializable(Utils.MQTT_HELPER_KEY);

            ArrayList<Note> titleNotes = (ArrayList<Note>) getArguments().getSerializable(Utils.LIST_NOTES_KEY);
            ArrayList<Note> searchTitleNotes = (ArrayList<Note>) getArguments().getSerializable(Utils.LIST_SEARCH_NOTES_KEY);

            if (titleNotes != null)
                addNoteTitlesList(titleNotes);

            if (searchTitleNotes != null)
                addSearchNoteTitlesList(searchTitleNotes);
        } else {
            noteKeeperDBHelper = new NoteKeeperDBHelper(getActivity());
            mqttHelper = new MqttHelper(noteKeeperDBHelper, new NoteActivity());
        }
    }

    private void initViewElements(View view) {
        tvTotalNotes = view.findViewById(R.id.tv_total_notes);
        etSearch = view.findViewById(R.id.et_search);
        rvNotesList = view.findViewById(R.id.recycler_notes);
        ivAdd = view.findViewById(R.id.iv_add);
        btnConnections = view.findViewById(R.id.btn_connections);
    }

    private void populateView() {
        tvTotalNotes.setText(getString(R.string.total_notes, String.valueOf(noteTitlesSearchList.size())));

        // Setup da Recycler View
        rvNotesListLayoutManager = new LinearLayoutManager(getContext());
        rvNotesList.setLayoutManager(rvNotesListLayoutManager);
        rvNotesListAdapter = new NoteListAdapter(noteTitlesSearchList);
        rvNotesList.setAdapter(rvNotesListAdapter);
    }

    private void editNote(int index) {
        final EditText edittext = new EditText(getContext());
        edittext.setHint(R.string.change_dialog_edit_hint);

        // Apresentar a dialog para o utilizador introduzir novo titulo
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.change_dialog_title)
                .setView(edittext)
                .setPositiveButton(R.string.change_dialog_confirm, (dialog, whichButton) -> {

                    String newTitle = edittext.getText().toString();

                    if (!newTitle.isEmpty()) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Utils.DATABASE_HELPER_KEY, noteKeeperDBHelper);
                        bundle.putSerializable(Utils.MQTT_HELPER_KEY, mqttHelper);
                        bundle.putString(Utils.NOTE_TITLE_KEY, newTitle);
                        bundle.putString(Utils.OPERATION_KEY, Utils.CHANGE_NOTE_MODE);
                        bundle.putSerializable(Utils.ACTIVITY_KEY, (Serializable) getActivity());
                        bundle.putSerializable(Utils.NOTE_KEY, noteTitlesSearchList.get(index));
                        new SaveNoteTask(getActivity(), bundle).execute();
                    }
                })
                .setNegativeButton(R.string.change_dialog_cancel, (dialog, whichButton) -> {
                }).show();
    }

    private void deleteNote(int index) {
        // Apresentar a dialog para o utilizador apagar ou não
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.delete_dialog_title)
                .setMessage(R.string.delete_dialog_content)
                .setPositiveButton(R.string.delete_dialog_confirm, (dialog, which) -> {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Utils.DATABASE_HELPER_KEY, noteKeeperDBHelper);
                    bundle.putSerializable(Utils.ACTIVITY_KEY, (Serializable) getActivity());
                    bundle.putSerializable(Utils.NOTE_KEY, noteTitlesSearchList.get(index));
                    new DeleteNoteTask(getActivity(), bundle).execute();

                }).setNegativeButton(R.string.delete_dialog_cancel, (dialog, which) -> {
            dialog.dismiss();
        }).show();
    }

    private void addNoteTitlesList(ArrayList<Note> titleNotes) {
        for (Note note : titleNotes)
            if (!noteTitlesList.contains(note))
                noteTitlesList.add(note);
    }

    private void addSearchNoteTitlesList(ArrayList<Note> searchTitleNotes) {
        for (Note note : searchTitleNotes)
            if (!noteTitlesSearchList.contains(note))
                noteTitlesSearchList.add(note);
    }

    public interface OnNotesListFragmentInteractionListener {
        void OnNotesListFragmentInteraction(Bundle bundle);

        void OnConnectionsFragmentInteraction(Bundle bundle);
    }
}
