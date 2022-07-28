package com.example.mynewnoteapp.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mynewnoteapp.Model.NoteServiceAuth;

public class UpdateNoteViewModelFactory implements ViewModelProvider.Factory {
    private NoteServiceAuth noteService;

    public UpdateNoteViewModelFactory(NoteServiceAuth noteService) {
        this.noteService = noteService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new UpdateNoteVIewModel(noteService);
    }
}
