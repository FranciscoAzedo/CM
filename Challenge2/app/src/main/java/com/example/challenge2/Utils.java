package com.example.challenge2;

import java.util.UUID;

public abstract class Utils {
    public static String generateUUID() {
        return String.valueOf(UUID.randomUUID());
    }

    public static String getNoteTitle(String uuidNoteTitle) {
        return uuidNoteTitle != null ? uuidNoteTitle.split("-")[0] : null;
    }
}
