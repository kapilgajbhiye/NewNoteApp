package com.example.mynewnoteapp.DAO;

import com.example.mynewnoteapp.Model.AuthListener;
import com.example.mynewnoteapp.Model.Note;
import com.example.mynewnoteapp.Model.NoteAuthListener;

import java.util.ArrayList;

public interface NoteDao {
    public void storeNoteDatabase(Note note, AuthListener listener);
    public void updateNoteDatabase(Note note, AuthListener listener);
    public ArrayList<Note> readNoteDatabase(NoteAuthListener listener);
    public void deleteNote(String noteid);

}
