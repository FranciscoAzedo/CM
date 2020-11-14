package com.example.challenge2.view.fragment;

import android.content.Context;
import android.os.Bundle;
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
    private String noteContent;

    public static NoteDetailedFragment newInstance() {
        return new NoteDetailedFragment();
    }

    private void initArguments() {
        if (getArguments() != null) {
            noteTitle = getArguments().getString("title");
            noteContent = getArguments().getString("content");
            edit = getArguments().getBoolean("edit");
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
        etNoteContent.setText(noteContent);
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
        etNoteContent.setText(noteContent);

        // Listener para quando existir um clique para voltar atrás
        ivBack.setOnClickListener(v -> {
            // Voltar ao fragment anterior
            mListener.OnNoteDetailsFragmentInteraction();
        });

        // Listener para quando existir um clique para guardar a nota atual
        llSave.setOnClickListener(v -> {

            Bundle bundle = new Bundle();
            bundle.putString("content", etNoteContent.getText().toString());

            // Se se estiver a editar uma nota
            if (edit) {

                // Caso título seja diferente apagar titulo e ficheiro da nota original e criar nova nota
                if (!Utils.getNoteTitle(noteTitle).equals(etNoteTitle.getText().toString())) {

                    // Gerar título com ID
                    String uuidNoteTitle = etNoteTitle.getText().toString() + "-" + Utils.generateUUID();

                    // Colocar no bundle informação sobre nota
                    bundle.putString("title", noteTitle);
                    bundle.putString("uuidTitle", uuidNoteTitle);

                    Utils.updateNotes("CHANGE NOTE", getActivity(), bundle);
                }
                // Caso título seja igual atualizar só o conteúdo do ficheiro
                else {
                    // Guardar dados
                    bundle.putString("uuidTitle", noteTitle);
                    Utils.updateNotes("CHANGE CONTENT", getActivity(), bundle);
                }
            }
            // Se se estiver a criar uma nota
            else {
                // Gerar título com ID
                String uuidNoteTitle = etNoteTitle.getText().toString() + "-" + Utils.generateUUID();

                // Colocar no bundle informação sobre nota
                bundle.putString("uuidTitle", uuidNoteTitle);

                // Guardar dados
                Utils.updateNotes("CREATE NOTE", getActivity(), bundle);
            }

            // Voltar ao fragment anterior
            mListener.OnNoteDetailsFragmentInteraction();
        });

        // Listener para quando existir um clique para eliminar a nota atual
        llDelete.setOnClickListener(v -> {

            // Colocar no bundle informação de nota a apagar
            Bundle bundle = new Bundle();
            bundle.putString("title", noteTitle);

            Utils.updateNotes("DELETE NOTE", getActivity(), bundle);

            // Voltar ao fragment anterior
            mListener.OnNoteDetailsFragmentInteraction();
        });
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