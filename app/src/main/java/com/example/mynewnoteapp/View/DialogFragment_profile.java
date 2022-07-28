package com.example.mynewnoteapp.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.mynewnoteapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class DialogFragment_profile extends AppCompatDialogFragment {
    private static final int RESULT_SELECT_IMG = 1 ;
    private ImageView profileImage;
    private TextView profileName;
    private TextView profileEmail;
    private Button logoutBtn, editProfile;
    private String userId;
    private FirebaseUser fUser;
    private FirebaseAuth fAuth;
    private FirebaseFirestore firestore;
    private DocumentReference reference;
    private StorageReference storageReference;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_profile, container, false);
        profileImage = view.findViewById(R.id.myImg);
        profileName = view.findViewById(R.id.profilename);
        profileEmail = view.findViewById(R.id.profilemail);
        logoutBtn = view.findViewById(R.id.btn_logout);
        editProfile = view.findViewById(R.id.btn_editprofile);
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("Users/"+fAuth.getCurrentUser().getUid() +"/profile.jpeg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });
        userFetchingData();
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                dismiss();
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open gallery
                selectImageFromGallary();
            }
        });
        return view;
    }

    public void userFetchingData(){
        String currentId = fUser.getUid();

        reference = firestore.collection("Users").document(currentId);
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    String nameResult = task.getResult().getString("fName");
                    String emailResult = task.getResult().getString("email");
                    profileName.setText(nameResult);
                    profileEmail.setText(emailResult);
                }
            }
        });
    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new LoginFragment()).commit();
    }

    private void selectImageFromGallary() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGalleryIntent, 100);

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), RESULT_SELECT_IMG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("APP_DEBUG", String.valueOf(requestCode));
        // When an Image is picked
        if (requestCode == RESULT_SELECT_IMG && resultCode == Activity.RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            //profileImage.setImageURI(selectedImage);
            uploadImageTOFirebase(selectedImage);
        }
    }

    private void uploadImageTOFirebase(Uri selectedImage) {
        //upload image to firebase storage
        StorageReference fileRef = storageReference.child("Users/"+fAuth.getCurrentUser().getUid() +"/profile.jpeg");
       fileRef.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImage);
                    }
                });
                Toast.makeText(getActivity(), "image uploaded", Toast.LENGTH_SHORT).show();
                //dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }
       });
    }
}