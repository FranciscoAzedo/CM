//package com.example.challenge2.model.AsyncTasks;
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//
//import com.example.challenge2.Utils;
//import com.example.challenge2.model.Repository.NoteKeeperDBHelper;
//import com.example.challenge2.view.NoteActivity;
//import com.example.challenge2.view.NotificationManager;
//
//import java.io.FileNotFoundException;
//
///**
// * Classe ReadNoteTask que representa a AsyncTask responsável por ler uma nota armazenada em memória
// */
//public class ReadNoteTask extends AsyncTask<Void, Void, Void> {
//
//    private final NoteKeeperDBHelper noteKeeperDBHelper;
//    private final NotificationManager notificationManager;
//    private final int noteId;
//    private final String noteTitle;
//
//    // Construtor da async task
//    public ReadNoteTask(Bundle bundle) {
//        this.noteKeeperDBHelper = (NoteKeeperDBHelper) bundle.getSerializable(Utils.DATABASE_HELPER_KEY);
//        this.notificationManager = (NoteActivity) bundle.getSerializable(Utils.ACTIVITY_KEY);
//    }
//
//    // Método executado quando a async task é chamada
//    @Override
//    protected Void doInBackground(Void... args) {
//        try {
//
//
//            if (mode == "ALL"){
//
//            }
//            noteContent = FileSystemManager.readNoteContent(activity, noteUUID);
//            bundle.putSerializable("noteContent", noteContent);
//        } catch (FileNotFoundException exception) {
//            this.exception = exception;
//        }
//        return null;
//    }
//}
