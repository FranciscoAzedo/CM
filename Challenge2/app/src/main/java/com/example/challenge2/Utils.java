package com.example.challenge2;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.challenge2.model.AsyncTasks.DeleteNoteTask;
import com.example.challenge2.model.AsyncTasks.SaveNoteTask;
import com.example.challenge2.model.NoteContent;
import com.example.challenge2.model.Repository.SharedPreferencesManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.UUID;

public abstract class Utils {

    public static final String SPLIT_STRING_PATTERN = "###";
    public static final String NOTE_CONTENT_FILE_NAME = "noteContents.json";
    public static final String TITLE_LIST_KEY = "titles";
    public static final String NOTE_TITLE_KEY = "noteTitle";
    public static final String NOTE_CONTENT_KEY = "noteContent";
    public static final String NOTE_UUID_KEY = "noteUUID";
    public static final String EDIT_MODE_KEY = "edit";
    public static final String CREATE_NOTE_MODE = "CREATE NOTE";
    public static final String CHANGE_NOTE_TITLE_MODE = "CHANGE NOTE TITLE";
    public static final String CHANGE_NOTE_CONTENT_MODE = "CHANGE NOTE CONTENT";
    public static final String CHANGE_NOTE_TITLE_AND_CONTENT_MODE = "CHANGE NOTE TITLE AND CONTENT";
    public static final String DELETE_NOTE_MODE = "DELETE NOTE";

    /**
     * Método que gere as operações de atualizar uma nota
     *
     * @param operation        operação a realizar
     * @param fragmentActivity
     * @param bundle
     * @throws FileNotFoundException exceção caso o ficheiro de notas não seja encontrado
     */
    public static void updateNotes(String operation, FragmentActivity fragmentActivity, Bundle bundle) throws FileNotFoundException {
        if (operation != null)
            switch (operation) {
                case CREATE_NOTE_MODE:
                    createTitleSharedPreference(fragmentActivity, bundle);
                    createNoteContent(fragmentActivity, bundle);
                    break;
                case CHANGE_NOTE_TITLE_MODE:
                    updateTitleSharedPreference(fragmentActivity, bundle);
                    break;
                case CHANGE_NOTE_CONTENT_MODE:
                    updateNoteContent(fragmentActivity, bundle);
                    break;
                case CHANGE_NOTE_TITLE_AND_CONTENT_MODE:
                    updateTitleSharedPreference(fragmentActivity, bundle);
                    updateNoteContent(fragmentActivity, bundle);
                    break;
                case DELETE_NOTE_MODE:
                    deleteTitleSharedPreference(fragmentActivity, bundle);
                    deleteNoteContent(fragmentActivity, bundle);
                    break;
            }
    }

    /**
     * Método para obter o título através do Identificador Único Universal de uma nota
     *
     * @param uuidNoteTitle identificador único universal da nota
     * @return título da nota
     */
    public static String getNoteTitle(String uuidNoteTitle) {
        return uuidNoteTitle != null ? uuidNoteTitle.split(Utils.SPLIT_STRING_PATTERN)[0] : null;
    }

    /**
     * Método para obter o Identificador Único Universal através do título de uma nota
     *
     * @param noteTitle título da nota
     * @return identificador único universal
     */
    public static UUID getUUIDFromTitle(String noteTitle) {
        return UUID.fromString(noteTitle.split(Utils.SPLIT_STRING_PATTERN)[1]);
    }

    /**
     * Método que serializa os conteúdos de uma lista de notas
     *
     * @param noteContents lista de conteúdos de notas
     * @return lista de conteúdos de notas serializado
     */
    public static String serializeListOfNoteContents(ArrayList<NoteContent> noteContents) {
        return new Gson().toJson(noteContents, new TypeToken<ArrayList<NoteContent>>() {
        }.getType());
    }

    /**
     * Método que deserializa os conteúdos de uma lista de notas
     *
     * @param noteContents lista de conteúdos de notas serializado
     * @return lista de conteúdos de notas
     */
    public static ArrayList<NoteContent> deserializeListOfNoteContents(String noteContents) {
        return new Gson().fromJson(noteContents, new TypeToken<ArrayList<NoteContent>>() {
        }.getType());
    }

    /**
     * Método que armazena um novo título nas Shared Preferences
     *
     * @param fragmentActivity
     * @param bundle
     */
    private static void createTitleSharedPreference(FragmentActivity fragmentActivity, Bundle bundle) {
        SharedPreferencesManager.saveSharedPreference(
                fragmentActivity,
                TITLE_LIST_KEY,
                bundle.getString(NOTE_TITLE_KEY)
        );
    }

    /**
     * Método que atualiza um título das Shared Preferences
     *
     * @param fragmentActivity
     * @param bundle
     */
    private static void updateTitleSharedPreference(FragmentActivity fragmentActivity, Bundle bundle) {
        SharedPreferencesManager.updateSharedPreference(
                fragmentActivity,
                TITLE_LIST_KEY,
                bundle.getString(NOTE_TITLE_KEY)
        );
    }

    /**
     * Método que elimina um título das Shared Preferences
     *
     * @param fragmentActivity
     * @param bundle
     */
    private static void deleteTitleSharedPreference(FragmentActivity fragmentActivity, Bundle bundle) {
        SharedPreferencesManager.removeSharedPreference(
                fragmentActivity,
                TITLE_LIST_KEY,
                bundle.getString(NOTE_TITLE_KEY)
        );
    }

    /**
     * Método que armazena o conteúdo de uma nova nota no armazenamento do dispositivo
     *
     * @param fragmentActivity
     * @param bundle
     */
    private static void createNoteContent(FragmentActivity fragmentActivity, Bundle bundle) {
        new SaveNoteTask(
                fragmentActivity,
                (NoteContent) bundle.getSerializable(NOTE_CONTENT_KEY),
                false
        ).execute();
    }

    /**
     * Método que atualiza o conteúdo de uma nota do armazenamento do dispositivo
     *
     * @param fragmentActivity
     * @param bundle
     */
    private static void updateNoteContent(FragmentActivity fragmentActivity, Bundle bundle) {
        new SaveNoteTask(
                fragmentActivity,
                (NoteContent) bundle.getSerializable(NOTE_CONTENT_KEY),
                true
        ).execute();
    }

    /**
     * Método que elimina o conteúdo de uma nota do armazenamento do dispositivo
     *
     * @param fragmentActivity
     * @param bundle
     */
    private static void deleteNoteContent(FragmentActivity fragmentActivity, Bundle bundle) {
        new DeleteNoteTask(
                fragmentActivity,
                UUID.fromString(bundle.getString(NOTE_UUID_KEY))
        ).execute();
    }
}
