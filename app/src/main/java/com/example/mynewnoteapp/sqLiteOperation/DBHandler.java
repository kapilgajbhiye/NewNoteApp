package com.example.mynewnoteapp.sqLiteOperation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mynewnoteapp.DAO.NoteDao;
import com.example.mynewnoteapp.Model.AuthListener;
import com.example.mynewnoteapp.Model.Note;
import com.example.mynewnoteapp.Model.NoteAuthListener;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper implements NoteDao {

    public static final String DB_NAME = "noteDB";
    public  static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "myNotes";
    public static final String TITLE_COLM = "title";
    public static final String NOTE_COLM = "note";
    public static final String DATE_COLM = "date" ;
    public static final  String NOTEID_COLM = "noteId";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + TITLE_COLM + " TEXT,"
                + NOTE_COLM + " TEXT,"
                + DATE_COLM + " TEXT,"
                + NOTEID_COLM + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void storeNoteDatabase(Note note, AuthListener listener) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE_COLM, note.getTitle());
        values.put(NOTE_COLM, note.getNote());
        values.put(DATE_COLM, note.getDate());
        values.put(NOTEID_COLM, note.getNoteId());

        db.insert(TABLE_NAME, null, values);
//        db.close();
    }

    @Override
    public ArrayList<Note> readNoteDatabase(NoteAuthListener listener) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        ArrayList<Note> arrNotes = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                arrNotes.add(new Note(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)));
            } while (cursor.moveToNext());
        }
   //     cursor.close();
        return arrNotes;

    }

    @Override
    public void updateNoteDatabase(Note note, AuthListener listener) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE_COLM, note.getTitle());
        values.put(NOTE_COLM, note.getNote());
        values.put(DATE_COLM, note.getDate());
        //values.put(NOTEID_COLM,note.getNoteId());

        db.update(TABLE_NAME, values, NOTEID_COLM + "= '" +note.getNoteId()+ "'" ,null);
 //       db.close();

    }

    @Override
    public void deleteNote(String noteid) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME,NOTEID_COLM + "=" +noteid,null);
 //       db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
