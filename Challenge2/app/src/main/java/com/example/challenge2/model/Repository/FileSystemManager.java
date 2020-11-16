package com.example.challenge2.model.Repository;

import android.content.Context;
import android.util.Log;

import com.example.challenge2.Utils;
import com.example.challenge2.model.NoteContent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

public abstract class FileSystemManager {

    public static void saveNoteContent(Context context, NoteContent noteContent) throws FileNotFoundException {
//        boolean fileExists = Utils.fileExist(Utils.NOTE_CONTENT_FILE_NAME, context);
//
//        if(fileExists) {
//             noteContents = readNoteFile(context);
//            if (noteContents == null)
//                noteContents = new ArrayList<>();
//        }

//        ArrayList<NoteContent> noteContents = new ArrayList<>();
        ArrayList<NoteContent> noteContents = readNoteFile(context);
        if (noteContents == null) {
            noteContents = new ArrayList<>();
        }
        noteContents.add(noteContent);

        FileSystemManager.printNoteContentFile(context);
        writeNoteContentToFile(context, noteContents);
        FileSystemManager.printNoteContentFile(context);
    }

    public static NoteContent readNoteContent(Context context, UUID noteUUID) throws FileNotFoundException {
        ArrayList<NoteContent> noteContents = readNoteFile(context);
        for (NoteContent noteContent : noteContents)
            if (noteContent.getNoteUUID().equals(noteUUID))
                return noteContent;
        return null;
    }

    public static void updateNoteContent(Context context, NoteContent noteContent) throws FileNotFoundException {
        ArrayList<NoteContent> noteContents = readNoteFile(context);
        for (NoteContent actualNoteContent : noteContents)
            if (actualNoteContent.getNoteUUID().equals(noteContent.getNoteUUID())) {
                actualNoteContent.setContent(noteContent.getContent());
                break;
            }
        writeNoteContentToFile(context, noteContents);
    }

    public static void removeNoteContent(Context context, UUID noteUUID) throws FileNotFoundException {
        ArrayList<NoteContent> noteContents = readNoteFile(context);

        for (NoteContent noteContent : noteContents)
            if (noteContent.getNoteUUID().equals(noteUUID)) {
                noteContents.remove(noteContent);
                break;
            }
        writeNoteContentToFile(context, noteContents);
    }

    private static ArrayList<NoteContent> readNoteFile(Context context) {

        Scanner scanner = null;
        try {
            scanner = new Scanner(context.openFileInput(Utils.NOTE_CONTENT_FILE_NAME));
        } catch (FileNotFoundException e) {
            return null;
        }

        StringBuilder notesContent = new StringBuilder();
        while (scanner.hasNextLine()) {
            notesContent.append(scanner.nextLine());
        }
        return Utils.deserializeListOfNoteContents(notesContent.toString());
    }

    private static void writeNoteContentToFile(Context context, ArrayList<NoteContent> noteContents) throws FileNotFoundException {

        PrintStream printStream = new PrintStream(context.openFileOutput(Utils.NOTE_CONTENT_FILE_NAME, Context.MODE_PRIVATE));
        printStream.println(Utils.serializeListOfNoteContents(noteContents));
        printStream.close();
    }

    public static void emptyNoteContentFile(Context context) throws FileNotFoundException {
        PrintStream printStream = new PrintStream(context.openFileOutput(Utils.NOTE_CONTENT_FILE_NAME, Context.MODE_PRIVATE));
        printStream.print("");
        printStream.close();
    }

    public static void printNoteContentFile(Context context) throws FileNotFoundException {
        ArrayList<NoteContent> noteContents = readNoteFile(context);
        if(noteContents == null) {
            return;
//            throw new FileNotFoundException();
        }
        for (NoteContent noteContent: noteContents) {
            Log.d(" Note Content", noteContent.toString());
        }
    }
}