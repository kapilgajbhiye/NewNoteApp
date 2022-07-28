package com.example.mynewnoteapp.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.mynewnoteapp.Model.Note;
import com.example.mynewnoteapp.Model.NoteServiceAuth;
import com.example.mynewnoteapp.R;
import com.example.mynewnoteapp.ViewModel.UpdateNoteVIewModel;
import com.example.mynewnoteapp.ViewModel.UpdateNoteViewModelFactory;
import com.example.mynewnoteapp.sqLiteOperation.DBHandler;

public class UpdateNoteFragment extends Fragment {
    private UpdateNoteVIewModel updateNoteViewModel;
    private String noteId, date,userId;
    private EditText editText_title, editText_note;
    private Button updateBtn;
    Note mynote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_update_note, container, false);
        ImageView backBtn = view.findViewById(R.id.imageView_backBtn1);
        ImageView notificationBtn = view.findViewById(R.id.imageView_notification);
        editText_title = view.findViewById(R.id.editText_Title1);
        editText_note = view.findViewById(R.id.editText_Note1);
        updateBtn = view.findViewById(R.id.updateBtn1);
        updateNoteViewModel = new ViewModelProvider(this, new UpdateNoteViewModelFactory(new NoteServiceAuth(new DBHandler(getContext())))).get(UpdateNoteVIewModel.class);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            editText_title.setText(bundle.getString("title"));
            editText_note.setText(bundle.getString("note"));
            date = bundle.getString("date");
            noteId = bundle.getString("noteId");
            userId = bundle.getString("userId");
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new HomeFragment()).commit();
            }
        });
        updateNoteInFirestore();
        return view;
    }

    private void updateNoteInFirestore() {

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editText_title.getText().toString();
                String note = editText_note.getText().toString();
                mynote = new Note(title, note, date, noteId);
                updateNoteViewModel.updateNotes(mynote);
                updateNoteViewModel.updateNoteStatus.observe(getViewLifecycleOwner(), Status -> {
                    if (Status.getStatus()) {
                        Toast.makeText(getActivity(), Status.getMsg(), Toast.LENGTH_SHORT).show();
                        getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new HomeFragment()).commit();
                    } else {
                        Toast.makeText(getActivity(), Status.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}