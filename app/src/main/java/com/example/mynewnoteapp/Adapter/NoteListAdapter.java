package com.example.mynewnoteapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynewnoteapp.Model.Note;
import com.example.mynewnoteapp.Model.NoteServiceAuth;
import com.example.mynewnoteapp.R;
import com.example.mynewnoteapp.View.UpdateNoteFragment;
import com.example.mynewnoteapp.sqLiteOperation.DBHandler;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.MyViewHolder> implements Filterable {
    Context context;
    ArrayList<Note> noteArrayList;
    ArrayList<Note> listOfNotes;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseUser fUser = fAuth.getCurrentUser();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    DBHandler dbHandler = new DBHandler(this.context);

    public NoteListAdapter(Context context, ArrayList<Note> noteArrayList) {
        this.context = context;
        this.noteArrayList = noteArrayList;
        this.listOfNotes = new ArrayList<>();
    }

    public void setNoteList(ArrayList<Note> noteList) {
        this.noteArrayList = noteList;
        listOfNotes.addAll(noteList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteListAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Note notes = noteArrayList.get(position);
        holder.title.setText(notes.getTitle());
        holder.note.setText(notes.getNote());
        holder.date.setText(notes.getDate());

        holder.menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(context, holder.menuIcon);
                popup.inflate(R.menu.option_menu);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.editNote:
                                UpdateNoteFragment updateNoteFragment = new UpdateNoteFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("title", notes.getTitle());
                                bundle.putString("note", notes.getNote());
                                bundle.putString("date", notes.getDate());
                                bundle.putString("noteId", notes.getNoteId());
                                bundle.putString("userID", notes.getUserId());
                                updateNoteFragment.setArguments(bundle);
                                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, updateNoteFragment).commit();
                                break;

                            case R.id.delNote:
                                noteArrayList.remove(position);
                                notifyDataSetChanged();
                                String docid = notes.getNoteId();
                                DocumentReference docref = firebaseFirestore.collection("Users").document(fUser.getUid()).collection("note").document(docid);
                                docref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "noteDeleted", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                dbHandler.deleteNote(docid);
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Note> filteredList = new ArrayList<>();
            if (constraint.toString().isEmpty()) {

                filteredList.addAll(listOfNotes);
                Log.d("adapter", "notes1 " + filteredList);
            } else {
                for (Note allNotes : listOfNotes) {
                    if (allNotes.getTitle().toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        listOfNotes.clear();
                        filteredList.add(allNotes);
                        Log.d("adapter", "notes " + allNotes);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            noteArrayList.clear();
            noteArrayList.addAll((Collection<? extends Note>) results.values);
            notifyDataSetChanged();
        }
    };

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, note, date;
        CardView myCardView;
        ImageView menuIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textView_title);
            note = itemView.findViewById(R.id.textView_notes);
            date = itemView.findViewById(R.id.textView_date);
            myCardView = itemView.findViewById(R.id.notes_Container);
            menuIcon = itemView.findViewById(R.id.imageView_pen);
        }
    }
}
