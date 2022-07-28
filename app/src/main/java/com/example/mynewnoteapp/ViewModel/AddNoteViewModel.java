package com.example.mynewnoteapp.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mynewnoteapp.Model.Indicate;
import com.example.mynewnoteapp.Model.Note;
import com.example.mynewnoteapp.Model.NoteServiceAuth;

public class AddNoteViewModel extends ViewModel {
    private NoteServiceAuth noteService;
    private MutableLiveData<Indicate> _noteUser = new MutableLiveData<>();
    public LiveData<Indicate> noteUserData = (LiveData<Indicate>) _noteUser;



    public AddNoteViewModel(NoteServiceAuth noteService){
        this.noteService = noteService;
    }

    public void saveNote(Note note){
        noteService.storeNoteDatabase(note, (Status, msg) -> _noteUser.setValue(new Indicate(Status, msg)));
    }

}
