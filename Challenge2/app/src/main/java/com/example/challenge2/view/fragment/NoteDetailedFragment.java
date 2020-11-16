package com.example.challenge2.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.challenge2.R;
import com.example.challenge2.Utils;
import com.example.challenge2.model.NoteContent;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.util.UUID;

public class NoteDetailedFragment extends Fragment {

    // View elements
    private ImageView ivBack;
    private EditText etNoteTitle;
    private EditText etNoteContent;
    private LinearLayout llSave;
    private LinearLayout llDelete;

    // Fragment listener
    private OnNoteDetailsFragmentInteractionListener mListener;

    private Boolean edit = false;
    private String noteTitle;
    private NoteContent noteContent;

    public static NoteDetailedFragment newInstance() {
        return new NoteDetailedFragment();
    }

    private void initArguments() {
        if (getArguments() != null) {
            noteTitle = getArguments().getString(Utils.NOTE_TITLE_KEY);
            noteContent = (NoteContent) getArguments().getSerializable(Utils.NOTE_CONTENT_KEY);
            edit = getArguments().getBoolean(Utils.EDIT_MODE_KEY);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Initialize arguments
        initArguments();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_detailed, container, false);
    }

    public void updateView() {
        initArguments();
        etNoteTitle.setText(Utils.getNoteTitle(noteTitle));
        etNoteContent.setText(noteContent != null ? noteContent.getContent() : null);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obter referências dos elementos visuais
        ivBack = view.findViewById(R.id.iv_back);
        etNoteTitle = view.findViewById(R.id.et_note_title);
        etNoteContent = view.findViewById(R.id.et_note_content);
        llSave = view.findViewById(R.id.ll_save_note);
        llDelete = view.findViewById(R.id.ll_delete_note);

        // Preencher informações da nota
        etNoteTitle.setText(Utils.getNoteTitle(noteTitle));
        etNoteContent.setText(noteContent != null ? noteContent.getContent() : null);

        // Listener para quando existir um clique para voltar atrás
        ivBack.setOnClickListener(v -> {
            // Voltar ao fragment anterior
            mListener.OnNoteDetailsFragmentInteraction();
        });

        // Listener para quando existir um clique para guardar a nota atual
        llSave.setOnClickListener(v -> {
            try {
                String updateNoteMode = null;
                Bundle bundle = new Bundle();

                // Se se estiver a editar uma nota
                if (edit) {
                    // Se o título for diferente
                    if (!Utils.getNoteTitle(noteTitle).equals(etNoteTitle.getText().toString())) {
                        // Se o conteudo for diferente
                        if (!noteContent.getContent().equals(etNoteContent.getText().toString()))
                            updateNoteMode = Utils.CHANGE_NOTE_TITLE_AND_CONTENT_MODE;
                        else
                            updateNoteMode = Utils.CHANGE_NOTE_TITLE_MODE;
                    }
                    // Se o conteudo for diferente
                    else if (!noteContent.getContent().equals(etNoteContent.getText().toString()))
                        updateNoteMode = Utils.CHANGE_NOTE_CONTENT_MODE;

                    noteContent.setContent(etNoteContent.getText().toString());
                    noteTitle = etNoteTitle.getText().toString() + Utils.SPLIT_STRING_PATTERN + Utils.getUUIDFromTitle(noteTitle);
                }

                // Se se estiver a criar uma nota
                else {
                    updateNoteMode = Utils.CREATE_NOTE_MODE;
                    UUID noteUUID = UUID.randomUUID();
                    noteContent = new NoteContent(noteUUID, etNoteContent.getText().toString());

                    // Gerar título com ID
                    noteTitle = etNoteTitle.getText().toString() + Utils.SPLIT_STRING_PATTERN + noteUUID;
                }

                bundle.putString(Utils.NOTE_TITLE_KEY, noteTitle);
                bundle.putSerializable(Utils.NOTE_CONTENT_KEY, noteContent);

                // Guardar dados
                Utils.updateNotes(updateNoteMode, getActivity(), bundle);

            } catch (FileNotFoundException exception) {
                notifyException(exception);
            } finally {
                // Voltar ao fragment anterior
                mListener.OnNoteDetailsFragmentInteraction();
            }
        });

        // Listener para quando existir um clique para eliminar a nota atual
        llDelete.setOnClickListener(v -> {
            try {
                // Colocar no bundle informação de nota a apagar
                Bundle bundle = new Bundle();
                bundle.putString(Utils.NOTE_TITLE_KEY, noteTitle);
                bundle.putString(Utils.NOTE_UUID_KEY, String.valueOf(Utils.getUUIDFromTitle(noteTitle)));
                Utils.updateNotes(Utils.DELETE_NOTE_MODE, getActivity(), bundle);
            } catch (FileNotFoundException exception) {
                notifyException(exception);
            } finally {
                // Voltar ao fragment anterior
                mListener.OnNoteDetailsFragmentInteraction();
            }
        });
    }

    private void notifyException(Exception exception) {
        Log.e("EXCEPTION", "Exception " + exception + "has occurred!");
        Snackbar.make(getActivity().getWindow().getDecorView().findViewById(R.id.RelativeLayout),
                R.string.read_note_error,
                Snackbar.LENGTH_SHORT)
                .show();
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

    public interface OnNoteDetailsFragmentInteractionListener {
        void OnNoteDetailsFragmentInteraction();
    }
}