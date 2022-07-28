package com.example.mynewnoteapp.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mynewnoteapp.Model.Note;
import com.example.mynewnoteapp.Model.NoteServiceAuth;
import com.example.mynewnoteapp.R;
import com.example.mynewnoteapp.ViewModel.AddNoteViewModel;
import com.example.mynewnoteapp.ViewModel.AddNoteViewModelFactory;
import com.example.mynewnoteapp.sqLiteOperation.DBHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNoteFragment extends Fragment {
    private EditText editText_title, editText_notes;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseUser fUser = fAuth.getCurrentUser();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    DocumentReference docref;
    private AddNoteViewModel notesViewModel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_note, container, false);
        ImageView imageView_save = view.findViewById(R.id.imageView_saves);
        ImageView imageView_Back = view.findViewById(R.id.imageView_back);
        editText_title = view.findViewById(R.id.editText_Title);
        editText_notes = view.findViewById(R.id.editText_note);
        notesViewModel = new ViewModelProvider(this, new AddNoteViewModelFactory(new NoteServiceAuth(new DBHandler(getContext())))).get(AddNoteViewModel.class);
        imageView_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeNoteInFireStore();

                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new HomeFragment()).commit();
            }
        });

        imageView_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new HomeFragment()).commit();
            }
        });
        return view;
    }

    private void storeNoteInFireStore() {
        String title = editText_title.getText().toString();
        String note = editText_notes.getText().toString();

        if(title.isEmpty() || note.isEmpty()){
            Toast.makeText(getActivity(), "please add some notes", Toast.LENGTH_SHORT).show();
            return;
        }
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        notesViewModel.saveNote(new Note(title, note, date));
        notesViewModel.noteUserData.observe(AddNoteFragment.this, status -> {
            if(status.getStatus()){
                Toast.makeText(getActivity(), status.getMsg(), Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getActivity(), status.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
