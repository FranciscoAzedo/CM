package com.example.challenge2.model.Repository;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public abstract class FileSystemManager {

    public static int createNoteFile(Context context, String noteTitle, String noteContent){
        try {
            PrintStream printStream = new PrintStream(context.openFileOutput(noteTitle + ".txt", context.MODE_PRIVATE));
            printStream.println(noteContent);
            printStream.close();
            return 1;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String readNoteFile (Context context, String noteTitle){
        try {
            Scanner scanner = new Scanner(context.openFileInput(noteTitle + ".txt"));
            StringBuilder note = new StringBuilder();
            while(scanner.hasNextLine()){
                note.append(scanner.nextLine());
            }
            return note.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void removeNoteFile(Context context, String noteTitle){
        context.deleteFile(noteTitle + ".txt");
    }
}