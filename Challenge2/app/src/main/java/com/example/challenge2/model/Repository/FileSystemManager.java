//package com.example.challenge2.model.Repository;
//
//import android.content.Context;
//
//import com.example.challenge2.Utils;
//import com.example.challenge2.model.NoteContent;
//
//import java.io.FileNotFoundException;
//import java.io.PrintStream;
//import java.util.ArrayList;
//import java.util.Scanner;
//import java.util.UUID;
//
///**
// * Classe FileSystemManager responsável por gerir os acessos de leitura/escrita a ficheiros
// * armazenados na memória do dispositivo
// */
//public abstract class FileSystemManager {
//
//    /**
//     * Método que guarda o conteúdo de uma nova nota no ficheiro
//     *
//     * @param context     contexto da aplicação
//     * @param noteContent conteúdo da nota
//     * @throws FileNotFoundException exceção caso o ficheiro de notas não seja encontrado
//     */
//    public static void saveNoteContent(Context context, NoteContent noteContent) throws FileNotFoundException {
//        ArrayList<NoteContent> noteContents = readNoteFile(context);
//        if (noteContents == null) {
//            noteContents = new ArrayList<>();
//        }
//        noteContents.add(noteContent);
//        writeNoteContentToFile(context, noteContents);
//    }
//
//    /**
//     * Método para ler o conteúdo de uma nota do ficheiro de acordo com o seu Identificador
//     * Único Universal
//     *
//     * @param context  contexto da aplicação
//     * @param noteUUID identificador único universal da nota a ser lida
//     * @return conteúdo da nota lida
//     * @throws FileNotFoundException exceção caso o ficheiro de notas não seja encontrado
//     */
//    public static NoteContent readNoteContent(Context context, UUID noteUUID) throws FileNotFoundException {
//        ArrayList<NoteContent> noteContents = readNoteFile(context);
//        for (NoteContent noteContent : noteContents)
//            if (noteContent.getNoteUUID().equals(noteUUID))
//                return noteContent;
//        return null;
//    }
//
//    /**
//     * Método para atualizar o conteúdo de uma nota no ficheiro
//     *
//     * @param context     contexto da aplicação
//     * @param noteContent conteúdo da nota
//     * @throws FileNotFoundException exceção caso o ficheiro de notas não seja encontrado
//     */
//    public static void updateNoteContent(Context context, NoteContent noteContent) throws FileNotFoundException {
//        ArrayList<NoteContent> noteContents = readNoteFile(context);
//        for (NoteContent actualNoteContent : noteContents)
//            if (actualNoteContent.getNoteUUID().equals(noteContent.getNoteUUID())) {
//                actualNoteContent.setContent(noteContent.getContent());
//                break;
//            }
//        writeNoteContentToFile(context, noteContents);
//    }
//
//    /**
//     * Método para eliminar uma nota do ficheiro
//     *
//     * @param context  contexto da aplicação
//     * @param noteUUID identificador único universal da nota a ser eliminada
//     * @throws FileNotFoundException exceção caso o ficheiro de notas não seja encontrado
//     */
//    public static void removeNoteContent(Context context, UUID noteUUID) throws FileNotFoundException {
//        ArrayList<NoteContent> noteContents = readNoteFile(context);
//
//        for (NoteContent noteContent : noteContents)
//            if (noteContent.getNoteUUID().equals(noteUUID)) {
//                noteContents.remove(noteContent);
//                break;
//            }
//        writeNoteContentToFile(context, noteContents);
//    }
//
//    /**
//     * Método que lê o ficheiro onde são armazenadas as notas
//     *
//     * @param context contexto da aplicação
//     * @return lista das notas existentes no ficheiro
//     */
//    private static ArrayList<NoteContent> readNoteFile(Context context) {
//
//        Scanner scanner;
//        try {
//            scanner = new Scanner(context.openFileInput(Utils.NOTE_CONTENT_FILE_NAME));
//        } catch (FileNotFoundException e) {
//            return null;
//        }
//
//        StringBuilder notesContent = new StringBuilder();
//        while (scanner.hasNextLine()) {
//            notesContent.append(scanner.nextLine());
//        }
//        return Utils.deserializeListOfNoteContents(notesContent.toString());
//    }
//
//    /**
//     * Método que escreve uma lista de conteúdos de notas no ficheiro
//     *
//     * @param context      contexto da aplicação
//     * @param noteContents lista dos conteúdos de notas
//     * @throws FileNotFoundException exceção caso o ficheiro de notas não seja encontrado
//     */
//    private static void writeNoteContentToFile(Context context, ArrayList<NoteContent> noteContents) throws FileNotFoundException {
//        PrintStream printStream = new PrintStream(context.openFileOutput(Utils.NOTE_CONTENT_FILE_NAME, Context.MODE_PRIVATE));
//        printStream.println(Utils.serializeListOfNoteContents(noteContents));
//        printStream.close();
//    }
//}