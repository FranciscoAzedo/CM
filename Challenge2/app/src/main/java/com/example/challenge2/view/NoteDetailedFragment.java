package com.example.challenge2.view;

import android.content.Context;
import android.os.AsyncTask;
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
import com.example.challenge2.model.Repository.FileSystemManager;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class NoteDetailedFragment extends Fragment {

    // View elements
    private ImageView ivBack;
    private EditText etNoteTitle;
    private EditText etNoteContent;
    private LinearLayout llSave;
    private LinearLayout llDelete;
    private Snackbar sbError;

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

        // Inicializar snackbar de erro
        sbError = Snackbar.make(view.findViewById(R.id.RelativeLayout), R.string.save_note_error, Snackbar.LENGTH_SHORT);

        // Listener para quando existir um clique para voltar atrás
        ivBack.setOnClickListener(v -> {
            getParentFragmentManager().popBackStackImmediate();
        });

        // Listener para quando existir um clique para guardar a nota atual
        llSave.setOnClickListener(v -> {
            new SaveNoteTask(getContext(), etNoteTitle.getText().toString(), etNoteContent.getText().toString()).execute();
            getParentFragmentManager().popBackStackImmediate();
        });

        // Listener para quando existir um clique para eliminar a nota atual
        llDelete.setOnClickListener(v -> {
            getParentFragmentManager().popBackStackImmediate();
        });
    }

    private class SaveNoteTask extends AsyncTask<Void, Void, Void>{

        private Exception exception;
        private Context context;
        private String title;
        private String content;

        // Construtor da async task
        SaveNoteTask(Context context, String title, String content){
            this.context = context;
            this.title = title;
            this.content = content;
        }

        // Método executado no final da async task
        @Override
        protected void onPostExecute(Void arg){
            if (exception != null) {
                Log.e("EXCEPTION", "Exception " + exception + "has occurred!");
                sbError.show();
            }
        }

        // Método executado quando a async task é chamada
        @Override
        protected Void doInBackground(Void... args) {
            try {
                FileSystemManager.createNoteFile(context, title, content);
            } catch (FileNotFoundException exception) {
                this.exception = exception;
            }
            return null;
        }
    }
}