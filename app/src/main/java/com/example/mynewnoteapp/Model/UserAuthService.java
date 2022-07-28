package com.example.mynewnoteapp.Model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserAuthService {
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseUser fUser = fAuth.getCurrentUser();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public void loginUser(User user, AuthListener listener){
        fAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    listener.onAuthComplete(true, "user login Successfully");
                }else{
                    listener.onAuthComplete(false, "login failed");
                }
            }
        });
    }

    public void resetPassUser(String email, AuthListener listener){
        fAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                listener.onAuthComplete(true, "Reset Link Send to your Email");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onAuthComplete(true, "Error ! Reset link is not send");
            }
        });
    }

    public void registrationUser(User user, AuthListener listener){
        fAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    listener.onAuthComplete(true, "Registration Successful");
                    performFireStoreOperation(user);
                }else{
                    //   progressDialog.dismiss();
                    listener.onAuthComplete(true, "Registration Failed"+task.getException());
                }
            }
        });
    }

    public void performFireStoreOperation(User user){
        String  userId = fAuth.getCurrentUser().getUid();
//        DocumentReference documentReference = firebaseFirestore.collection("Users").document();
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("fName", user.getFname());
        userDetails.put("lName", user.getLname());
        userDetails.put("email", user.getEmail());
        firebaseFirestore.collection("Users").document(fUser.getUid()).set(userDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void Void) {
            //    listener.onAuthComplete(true, "Successful");
            }
        });
    }
}

