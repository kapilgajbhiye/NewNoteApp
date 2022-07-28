package com.example.mynewnoteapp.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mynewnoteapp.Model.NoteServiceAuth;

public class HomeViewModelFactory implements ViewModelProvider.Factory {
    private NoteServiceAuth noteServiceAuth;

    public HomeViewModelFactory(NoteServiceAuth noteServiceAuth){
        this.noteServiceAuth = noteServiceAuth;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new HomeViewModel(noteServiceAuth);
    }
}
