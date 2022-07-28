package com.example.mynewnoteapp.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynewnoteapp.Adapter.NoteListAdapter;
import com.example.mynewnoteapp.Model.Note;
import com.example.mynewnoteapp.Model.NoteServiceAuth;
import com.example.mynewnoteapp.R;
import com.example.mynewnoteapp.ViewModel.HomeViewModel;
import com.example.mynewnoteapp.ViewModel.HomeViewModelFactory;
import com.example.mynewnoteapp.sqLiteOperation.DBHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private NoteListAdapter noteListAdapter;
    private HomeViewModel homeViewModel;
    ArrayList<Note> noteList;
    FirebaseFirestore firebaseFirestore;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseUser fUser = fAuth.getCurrentUser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.addBtn);
        RecyclerView recyclerView = view.findViewById(R.id.recycleView1);
        homeViewModel = new ViewModelProvider(this, new HomeViewModelFactory(new NoteServiceAuth(new DBHandler(getContext())))).get(HomeViewModel.class);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        firebaseFirestore = FirebaseFirestore.getInstance();
        noteList = new ArrayList<Note>();
        noteListAdapter = new NoteListAdapter(getContext(), noteList);
        recyclerView.setAdapter(noteListAdapter);
        getNotesFromFireStore();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new AddNoteFragment()).commit();
            }
        });

        return view;
    }

    private void getNotesFromFireStore(){
        homeViewModel.readNote();
        homeViewModel.getNoteStatus.observe(getViewLifecycleOwner(), status -> {
//            Log.d("HomeFragment","noteSize "+status.getNoteList().size());
            if(status.getStatus() && !status.getNoteList().isEmpty()){
                Toast.makeText(getActivity(), status.getMsg(), Toast.LENGTH_SHORT).show();
                noteListAdapter.setNoteList(status.getNoteList());
            }else {
                Toast.makeText(getActivity(), status.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       switch(item.getItemId()){
           case R.id.searchbar:
               SearchView searchView = (SearchView)item.getActionView();
               searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                   @Override
                   public boolean onQueryTextSubmit(String query) {
                       return false;
                   }
                   @Override
                   public boolean onQueryTextChange(String newText) {
                       noteListAdapter.getFilter().filter(newText);
                       return false;
                   }
               });
               break;

           case R.id.grid_view:
               Toast.makeText(getActivity(), "Grid View", Toast.LENGTH_SHORT).show();
                break;

           case R.id.profileView:
               openProfileDialog();
               break;
       }
        return super.onOptionsItemSelected(item);
    }

    public void openProfileDialog(){
        DialogFragment_profile dialogFragment = new DialogFragment_profile();
        dialogFragment.show(getActivity().getSupportFragmentManager(),"dialog");
    }
}
