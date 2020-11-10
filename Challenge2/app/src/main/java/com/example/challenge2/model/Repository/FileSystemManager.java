package com.example.challenge2.model.Repository;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public abstract class FileSystemManager {

    public static void createNoteFile(Context context, String noteTitle, String noteContent) throws FileNotFoundException {
            PrintStream printStream = new PrintStream(context.openFileOutput(noteTitle + ".txt", context.MODE_PRIVATE));
            printStream.println(noteContent);
            printStream.close();
    }

    public static String readNoteFile (Context context, String noteTitle) throws FileNotFoundException {
            Scanner scanner = new Scanner(context.openFileInput(noteTitle + ".txt"));
            StringBuilder note = new StringBuilder();
            while(scanner.hasNextLine()){
                note.append(scanner.nextLine());
            }
            return note.toString();
    }

    public static void removeNoteFile(Context context, String noteTitle){
        context.deleteFile(noteTitle + ".txt");
    }
}