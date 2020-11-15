package com.example.challenge2.model;

import java.io.Serializable;
import java.util.UUID;

public class NoteContent implements Serializable {

    private UUID noteUUID;
    private String content;

    public NoteContent(UUID noteUUID, String content) {
        this.noteUUID = noteUUID;
        this.content = content;
    }

    public UUID getNoteUUID() {
        return noteUUID;
    }

    public void setNoteUUID(UUID noteUUID) {
        this.noteUUID = noteUUID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
