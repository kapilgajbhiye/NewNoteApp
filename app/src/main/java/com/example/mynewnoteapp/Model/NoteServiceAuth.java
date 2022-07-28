package com.example.mynewnoteapp.Model;


import androidx.annotation.NonNull;

import com.example.mynewnoteapp.DAO.NoteDao;
import com.example.mynewnoteapp.Utilities.NetworkConnection;
import com.example.mynewnoteapp.View.MainActivity;
import com.example.mynewnoteapp.sqLiteOperation.DBHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NoteServiceAuth implements NoteDao {

    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseUser fUser = fAuth.getCurrentUser();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DocumentReference docref;
    private DBHandler dbHandler;
    MainActivity mainActivity = new MainActivity();
    public NoteServiceAuth(DBHandler dbHandler){
        this.dbHandler=dbHandler;
    }

    public void storeNoteDatabase(Note note, AuthListener listener){
        docref = firebaseFirestore.collection("Users").document(fUser.getUid()).collection("note").document();
        Map<String, Object> mynote = new HashMap<>();
        mynote.put("title", note.getTitle());
        mynote.put("note", note.getNote());
        mynote.put("date",note.getDate());
        note.setUserId(fUser.getUid());
        mynote.put("userId",note.getUserId());
        note.setNoteId(docref.getId());
        mynote.put("noteId",note.getNoteId());

        docref.set(mynote).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful() && mainActivity.isConnected()){
                    dbHandler.storeNoteDatabase(note,listener);
                    listener.onAuthComplete(true, "Data added Successfully");
                }
                else{
                    listener.onAuthComplete(false, "Failed");
                    dbHandler.storeNoteDatabase(note,listener);
                }
            }
        });
    }

    public ArrayList<Note> readNoteDatabase(NoteAuthListener listener) {
        firebaseFirestore.collection("Users").document(fUser.getUid()).collection("note").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
               ArrayList<Note> noteList = new ArrayList<>();
                if(task.isSuccessful() && task.getResult() != null ){
                    dbHandler.readNoteDatabase(listener);
                    for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                        Note note = new Note(documentSnapshot.getString("title"),documentSnapshot.getString("note"),documentSnapshot.getString("date"),documentSnapshot.getString("noteId"));
                        noteList.add(note);
                        listener.onAuthComplete(true, "Fetch notes successfully", noteList);
                    }
                }
            }
        });
        return null;
    }

    public void updateNoteDatabase(Note note, AuthListener listener){
        Map<String, Object> notes = new HashMap<>();
        notes.put("title", note.getTitle());
        notes.put("note",note.getNote());
        firebaseFirestore.collection("Users").document(fAuth.getUid()).collection("note").document(note.getNoteId()).update(notes).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    dbHandler.updateNoteDatabase(note, listener);
                    listener.onAuthComplete(true, "note Updated Successfully");
                }else{
                    listener.onAuthComplete(false,"Failed");
                }
            }
        });
    }

    @Override
    public void deleteNote(String noteid) {
//        DocumentReference docref  = firebaseFirestore.collection("Users").document(fUser.getUid()).collection("note").document(note.getNoteId());
//        docref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                dbHandler.deleteNote(note);
//              Toast.makeText(context, "noteDeleted", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
