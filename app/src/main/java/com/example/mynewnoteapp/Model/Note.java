package com.example.mynewnoteapp.Model;

public class Note {
    private String title;
    private String note;
    private String date, noteId,userId;

    public Note(String title, String note, String date) {
        this.title = title;
        this.note = note;
        this.date = date;
    }

    public Note(String title, String note, String date, String noteId) {
        this.title = title;
        this.note = note;
        this.date = date;
        this.noteId = noteId;
    }

    public Note(String title, String notes) {
        this.title=title;
        this.note = notes;
    }

    public Note(String title, String note, String date, String noteId, String userId) {
        this.title = title;
        this.note = note;
        this.date = date;
        this.noteId = noteId;
        this.userId = userId;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
