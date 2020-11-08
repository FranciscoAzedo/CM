package com.example.challenge2.view;

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

public class NoteDetailedFragment extends Fragment {

    // View elements
    private ImageView ivBack;
    private EditText etNoteTitle;
    private EditText etNoteContent;
    private LinearLayout llSave;
    private LinearLayout llDelete;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_detailed, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obter referências dos elementos visuais
        ivBack = view.findViewById(R.id.iv_back);
        etNoteTitle = view.findViewById(R.id.et_note_title);
        etNoteContent = view.findViewById(R.id.et_note_content);
        llSave = view.findViewById(R.id.ll_save_note);
        llDelete = view.findViewById(R.id.ll_delete_note);

        // Listener para quando existir um clique para voltar atrás
        ivBack.setOnClickListener(v -> {
            getParentFragmentManager().popBackStackImmediate();
        });

        // Listener para quando existir um clique para guardar a nota atual
        llSave.setOnClickListener(v -> {
            getParentFragmentManager().popBackStackImmediate();
        });

        // Listener para quando existir um clique para eliminar a nota atual
        llDelete.setOnClickListener(v -> {
            getParentFragmentManager().popBackStackImmediate();
        });
    }
}