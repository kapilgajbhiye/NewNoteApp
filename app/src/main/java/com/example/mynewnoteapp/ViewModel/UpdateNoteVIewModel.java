package com.example.mynewnoteapp.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mynewnoteapp.Model.Indicate;
import com.example.mynewnoteapp.Model.Note;
import com.example.mynewnoteapp.Model.NoteServiceAuth;

public class UpdateNoteVIewModel extends ViewModel {

    private NoteServiceAuth noteService;
    private MutableLiveData<Indicate> _updateNoteStatus = new MutableLiveData<>();
    public LiveData<Indicate> updateNoteStatus = (LiveData<Indicate>) _updateNoteStatus;

    public UpdateNoteVIewModel(NoteServiceAuth noteService){
        this.noteService = noteService;

    }

    public void updateNotes(Note note){
        noteService.updateNoteDatabase(note, (Status, msg) -> _updateNoteStatus.setValue(new Indicate(Status, msg)));
    }
}
