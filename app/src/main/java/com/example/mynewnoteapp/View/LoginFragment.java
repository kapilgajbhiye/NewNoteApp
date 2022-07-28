package com.example.mynewnoteapp.View;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mynewnoteapp.Model.User;
import com.example.mynewnoteapp.Model.UserAuthService;
import com.example.mynewnoteapp.R;
import com.example.mynewnoteapp.ViewModel.LoginViewModel;
import com.example.mynewnoteapp.ViewModel.LoginViewModelFactory;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class LoginFragment extends Fragment {
    private static final int GOOGLE_SIGN_IN =1 ;
    private TextView registration_textView, forgotPassword;
    private EditText inputEmail, inputPassword;
    private Button signInBtn;
    private ProgressDialog progressDialog;
    private ImageView googleBtn;
  //  private GoogleSignInClient googleSignInClient;
    private LoginViewModel loginViewModel;
    private static final int REQ_ONE_TAP = 2;
//    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
//    private FirebaseUser fUser = fAuth.getCurrentUser();
//    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        registration_textView = view.findViewById(R.id.notRegister_textView);
        inputEmail = view.findViewById(R.id.userName_editText);
        inputPassword = view.findViewById(R.id.password1_editText);
        signInBtn = view.findViewById(R.id.loginButton);
        forgotPassword = view.findViewById(R.id.forgotPassword_textView);
        googleBtn = view.findViewById(R.id.imageViewBtn);
        progressDialog = new ProgressDialog(getActivity());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory(new UserAuthService())).get(LoginViewModel.class);

        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("72667544943-r2du79l8gefb52frukm3o5mgv074ldnd.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // Initialize sign in client
//        googleSignInClient = GoogleSignIn.getClient(getActivity(), googleSignInOptions);
//        googleBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Initialize sign in intent
//                Intent intent=googleSignInClient.getSignInIntent();
//                // Start activity for result
//                startActivityForResult(intent,100);
//            }
//        });

//        public void onStart() {
//            super.onStart();
//            // Check if user is signed in (non-null) and update UI accordingly.
//            FirebaseUser currentUser = fAuth.getCurrentUser();
//            updateUI(currentUser);
//        }

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter your Email");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @SuppressLint("FragmentLiveDataObserve")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String email = resetMail.getText().toString();
                        loginViewModel.resetPassword(email);
                        loginViewModel.userResetPass.observe(LoginFragment.this, status ->{
                            if(status.getStatus()){
                                Toast.makeText(getActivity(), status.getMsg(), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(), status.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                passwordResetDialog.create().show();
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();

            }
        });

        registration_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new RegistrationFragment()).commit();
            }
        });
        return view;
    }

    private void performLogin() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if(email.isEmpty()){
            inputEmail.setError("Email is Required");
        }
        else if(password.isEmpty() || password.length() < 6){
            inputPassword.setError("Password is Required");
        }
        else {
            progressDialog.setMessage("Login is processing....");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            loginViewModel.userLogin(new User(email, password));
            loginViewModel.userLoginStatus.observe(LoginFragment.this, status -> {
                if(status.getStatus()){
                    Toast.makeText(getActivity(), status.getMsg(), Toast.LENGTH_SHORT).show();
                    getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new HomeFragment()).commit();
                    progressDialog.hide();
                }else {
                    Toast.makeText(getActivity(), status.getMsg(), Toast.LENGTH_SHORT).show();
                }

            });
        }
    }

//    private void sendUserToMainActivity() {
//        Home home = new Home();
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(R.id.container, home);
//        ft.commit();
//    }

//    public void googleSignIn(){
//        Intent signInIntent = googleSignInClient.getSignInIntent();
//        getActivity().startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
//    }
//
//    public boolean isUserAlreadySignIn(){
//        FirebaseUser currentUser = fAuth.getCurrentUser();
//        if(currentUser != null){
//            return true;
//        }else{
//            return false;
//        }
//    }
//
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // Check condition
//        if(requestCode==100)
//        {
//            Task<GoogleSignInAccount> signInAccountTask=GoogleSignIn
//                    .getSignedInAccountFromIntent(data);
//
//            // check condition
//            if(signInAccountTask.isSuccessful())
//            {
//                String s="Google sign in successful";
//                displayToast(s);
//                try {
//                    GoogleSignInAccount googleSignInAccount=signInAccountTask
//                            .getResult(ApiException.class);
//                    // Check condition
//                    if(googleSignInAccount!=null)
//                    {
//                        AuthCredential authCredential= GoogleAuthProvider
//                                .getCredential(googleSignInAccount.getIdToken()
//                                        ,null);
//                        // Check credential
//                        fAuth.signInWithCredential(authCredential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>()
//                        {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if(task.isSuccessful())
//                                {
//                                    sendUserToMainActivity();
//                                    displayToast("Firebase authentication successful");
//                                }
//                                else
//                                {
//                                    displayToast("Authentication Failed :"+task.getException().getMessage());
//                                }
//                            }
//                        });
//                    }
//                }
//                catch (ApiException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//    public void displayToast(String s) {
//        Toast.makeText(getContext().getApplicationContext(),s,Toast.LENGTH_SHORT).show();
//    }
}
