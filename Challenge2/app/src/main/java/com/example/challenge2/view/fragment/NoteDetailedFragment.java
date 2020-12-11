package com.example.challenge2.view.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.challenge2.MqttHelper;
import com.example.challenge2.R;
import com.example.challenge2.Utils;
import com.example.challenge2.model.AsyncTasks.DeleteNoteTask;
import com.example.challenge2.model.AsyncTasks.SaveNoteTask;
import com.example.challenge2.model.Note;
import com.example.challenge2.model.Repository.NoteKeeperDBHelper;
import com.example.challenge2.view.NoteActivity;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.ArrayList;

public class NoteDetailedFragment extends Fragment {

    // View elements
    private ImageView ivBack;
    private EditText etNoteTitle;
    private EditText etNoteContent;
    private LinearLayout llSave;
    private LinearLayout llDelete;
    private AlertDialog alertDialog;

    // Fragment listener
    private OnNoteDetailsFragmentInteractionListener mListener;

    private ArrayList<Note> noteTitlesList = new ArrayList<>();
    private ArrayList<Note> noteTitlesSearchList = new ArrayList<>();

    private Boolean edit = false;
    private Note note;
    private NoteKeeperDBHelper noteKeeperDBHelper;
    private MqttHelper mqttHelper;

    public static NoteDetailedFragment newInstance() {
        return new NoteDetailedFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_detailed, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initArguments();
        initViewElements(view);
        populateView();

        // Listener para quando existir um clique para voltar atrás
        ivBack.setOnClickListener(v -> {
            // Voltar ao fragment anterior
            Bundle listenerBundle = new Bundle();
            listenerBundle.putSerializable(Utils.LIST_NOTES_KEY, noteTitlesList);
            listenerBundle.putSerializable(Utils.LIST_SEARCH_NOTES_KEY, noteTitlesSearchList);
            mListener.OnNoteDetailsFragmentInteraction(listenerBundle);
        });

        // Listener para quando existir um clique para guardar a nota atual
        llSave.setOnClickListener(v -> {
            saveNote();
        });

        // Listener para quando existir um clique para eliminar a nota atual
        llDelete.setOnClickListener(v -> {
            alertDialog = popConfirmationNoteDelete();
            alertDialog.show();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    public void updateView() {
        initArguments();
        populateView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNoteDetailsFragmentInteractionListener) {
            mListener = (OnNoteDetailsFragmentInteractionListener) context;
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

    private void initArguments() {
        if (getArguments() != null) {
            edit = getArguments().getBoolean(Utils.EDIT_MODE_KEY);
            note = (Note) getArguments().getSerializable(Utils.NOTE_KEY);
            noteKeeperDBHelper = (NoteKeeperDBHelper) getArguments().getSerializable(Utils.DATABASE_HELPER_KEY);
            mqttHelper = (MqttHelper) getArguments().getSerializable(Utils.MQTT_HELPER_KEY);
            noteTitlesList = (ArrayList<Note>) getArguments().getSerializable(Utils.LIST_NOTES_KEY);
            noteTitlesSearchList = (ArrayList<Note>) getArguments().getSerializable(Utils.LIST_SEARCH_NOTES_KEY);
        } else {
            note = new Note();
            noteKeeperDBHelper = new NoteKeeperDBHelper(getActivity());
            mqttHelper = new MqttHelper(noteKeeperDBHelper, new NoteActivity());
            noteTitlesList = new ArrayList<>();
            noteTitlesSearchList = new ArrayList<>();
        }
    }

    private void initViewElements(View view) {
        ivBack = view.findViewById(R.id.iv_back);
        etNoteTitle = view.findViewById(R.id.et_note_title);
        etNoteContent = view.findViewById(R.id.et_note_content);
        llSave = view.findViewById(R.id.ll_save_note);
        llDelete = view.findViewById(R.id.ll_delete_note);
    }

    private void populateView() {
        if (edit) {
            etNoteTitle.setText(note.getTitle());
            etNoteContent.setText(note.getContent());
        } else {
            etNoteTitle.setText("");
            etNoteContent.setText("");
        }
    }

    private void deleteNote() {

        Bundle bundle = new Bundle();
        bundle.putSerializable(Utils.DATABASE_HELPER_KEY, noteKeeperDBHelper);
        bundle.putSerializable(Utils.ACTIVITY_KEY, (Serializable) getActivity());
        bundle.putSerializable(Utils.NOTE_KEY, note);

        new DeleteNoteTask(getActivity(), bundle).execute();

        Bundle listenerBundle = new Bundle();
        listenerBundle.putSerializable(Utils.LIST_NOTES_KEY, noteTitlesList);
        listenerBundle.putSerializable(Utils.LIST_SEARCH_NOTES_KEY, noteTitlesSearchList);
        mListener.OnNoteDetailsFragmentInteraction(listenerBundle);
    }

    private AlertDialog popConfirmationNoteDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.delete_dialog_title)
                .setMessage(R.string.delete_dialog_content)
                .setPositiveButton(R.string.delete_dialog_confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteNote();
                    }
                })
                .setNegativeButton(R.string.delete_dialog_cancel, null);
        return builder.create();
    }

    private void saveNote() {

        if (etNoteTitle.getText().toString().equals("")) {
            Snackbar.make(getActivity().getWindow().getDecorView().findViewById(R.id.RelativeLayout),
                    R.string.empty_title_error,
                    Snackbar.LENGTH_SHORT)
                    .show();
            return;
        } else if (etNoteContent.getText().toString().equals("")) {
            Snackbar.make(getActivity().getWindow().getDecorView().findViewById(R.id.RelativeLayout),
                    R.string.empty_content_error,
                    Snackbar.LENGTH_SHORT)
                    .show();
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(Utils.DATABASE_HELPER_KEY, noteKeeperDBHelper);
        bundle.putSerializable(Utils.MQTT_HELPER_KEY, mqttHelper);
        bundle.putString(Utils.NOTE_TITLE_KEY, etNoteTitle.getText().toString());
        bundle.putString(Utils.NOTE_CONTENT_KEY, etNoteContent.getText().toString());
        bundle.putSerializable(Utils.ACTIVITY_KEY, (Serializable) getActivity());
        bundle.putSerializable(Utils.NOTE_KEY, note);

        if (edit) {
            bundle.putString(Utils.OPERATION_KEY, Utils.CHANGE_NOTE_MODE);
        } else {
            bundle.putString(Utils.OPERATION_KEY, Utils.CREATE_NOTE_MODE);
        }

        new SaveNoteTask(getActivity(), bundle).execute();


        Bundle listenerBundle = new Bundle();
        listenerBundle.putSerializable(Utils.LIST_NOTES_KEY, noteTitlesList);
        listenerBundle.putSerializable(Utils.LIST_SEARCH_NOTES_KEY, noteTitlesSearchList);
        mListener.OnNoteDetailsFragmentInteraction(listenerBundle);
    }

    public interface OnNoteDetailsFragmentInteractionListener {
        void OnNoteDetailsFragmentInteraction(Bundle bundle);
    }
}