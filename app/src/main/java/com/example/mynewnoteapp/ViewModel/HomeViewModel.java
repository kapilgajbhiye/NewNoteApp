package com.example.mynewnoteapp.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mynewnoteapp.Model.Indicate;
import com.example.mynewnoteapp.Model.Note;
import com.example.mynewnoteapp.Model.NoteServiceAuth;

public class HomeViewModel extends ViewModel {
    private  NoteServiceAuth noteServiceAuth;

    private MutableLiveData<Indicate> _getNoteStatus = new MutableLiveData<>();
    public LiveData<Indicate> getNoteStatus = (LiveData<Indicate>) _getNoteStatus;

    private MutableLiveData<Indicate> _delNoteStatus = new MutableLiveData<>();
    public LiveData<Indicate> delNoteStatus = (LiveData<Indicate>) _delNoteStatus;

    public HomeViewModel(NoteServiceAuth noteServiceAuth){
        this.noteServiceAuth = noteServiceAuth;
    }

    public void readNote(){
        noteServiceAuth.readNoteDatabase((status, msg, noteList) -> {
            _getNoteStatus.setValue(new Indicate(status,msg,noteList));});

    }

//    public void deleteNoteDatabase(Note note){
//        noteServiceAuth.deleteNote(note, (status, msg) -> _delNoteStatus.setValue(status,msg));
//
//    }

}
