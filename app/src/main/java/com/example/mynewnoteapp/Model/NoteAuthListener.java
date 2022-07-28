package com.example.mynewnoteapp.Model;

import java.util.ArrayList;

public interface NoteAuthListener {
    void onAuthComplete(boolean status, String msg, ArrayList<Note> noteList);
}