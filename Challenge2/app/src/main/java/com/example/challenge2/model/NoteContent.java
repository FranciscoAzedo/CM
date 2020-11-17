package com.example.challenge2.model;

import java.io.Serializable;
import java.util.UUID;

/**
 * Classe NoteContent que representa o modelo de uma Nota no projeto
 */
public class NoteContent implements Serializable {

    /**
     * Identificador Único Universal da Nota
     */
    private final UUID noteUUID;
    /**
     * Conteúdo Textual da Nota
     */
    private String content;

    /**
     * Construtor
     *
     * @param noteUUID identificador único universal da nota
     * @param content  conteúdo textual da nota
     */
    public NoteContent(UUID noteUUID, String content) {
        this.noteUUID = noteUUID;
        this.content = content;
    }

    /**
     * Método para obter o identificador único universal da nota
     *
     * @return identificador único universal da nota
     */
    public UUID getNoteUUID() {
        return noteUUID;
    }

    /**
     * Método para obter o conteúdo textual da nota
     *
     * @return conteúdo textual da nota
     */
    public String getContent() {
        return content;
    }

    /**
     * Método para atualizar o conteúdo textual da nota
     *
     * @param content novo conteúdo textual da nota
     */
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "NoteContent{" +
                "noteUUID=" + noteUUID +
                ", content='" + content + '\'' +
                '}';
    }
}
