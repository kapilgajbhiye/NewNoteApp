package com.example.mynewnoteapp.Model;

import java.util.ArrayList;

public class Indicate {
    private boolean status;
    private String msg;
    private ArrayList<Note> noteList;

    public Indicate(boolean status, String msg, ArrayList<Note> noteList) {
        this.status = status;
        this.msg = msg;
        this.noteList = noteList;
    }
    public Indicate(boolean status, String msg) {
        this.status=status;
        this.msg=msg;
    }

    public ArrayList<Note> getNoteList() {
        return noteList;
    }

    public void setNoteList(ArrayList<Note> noteList) {
        this.noteList = noteList;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
