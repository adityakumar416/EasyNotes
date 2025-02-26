package com.example.easynotes.interfaces;

import com.example.easynotes.dataClass.Notes;

public interface NotesClickListener {
    void updateNote(Notes notes);

    void longPressClickForDeleteNote(int notesId);

    void addNoteClick();
}
