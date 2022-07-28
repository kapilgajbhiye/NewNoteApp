package com.example.mynewnoteapp.View;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynewnoteapp.Model.User;
import com.example.mynewnoteapp.Model.UserAuthService;
import com.example.mynewnoteapp.R;
import com.example.mynewnoteapp.ViewModel.RegistrationViewModel;
import com.example.mynewnoteapp.ViewModel.RegistrationViewModelFactory;


public class RegistrationFragment extends Fragment {
    private EditText firstName, lastName, inputEmail, inputPassword;
    private Button submitBtn;
    private TextView signIn_textView;
    private ProgressDialog progressDialog;
    private RegistrationViewModel registrationViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        signIn_textView = view.findViewById(R.id.signIn);
        firstName = view.findViewById(R.id.firstName_editText);
        lastName = view.findViewById(R.id.lastName_editText);
        inputEmail = view.findViewById(R.id.email_editText);
        inputPassword = view.findViewById(R.id.password_editText);
        submitBtn = view.findViewById(R.id.submitBtn);
        progressDialog = new ProgressDialog(getActivity());
        registrationViewModel = new ViewModelProvider(this, new RegistrationViewModelFactory(new UserAuthService())).get(RegistrationViewModel.class);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRegistration();
            }
        });

        signIn_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().add(R.id.fragmentContainer, new LoginFragment()).commit();
            }
        });
        return view;
    }

    private void performRegistration() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String fname = firstName.getText().toString();
        String lname = lastName.getText().toString();
        if(email.isEmpty()){
            inputEmail.setError("Email is Required");
            Toast.makeText(getActivity(), "Email Error", Toast.LENGTH_SHORT).show();
        }
        else if(password.isEmpty() || password.length() < 6){
            inputPassword.setError("Password is Required");
        }
        else{
            progressDialog.setMessage("Registration is processing....");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            registrationViewModel.userRegistration(new User(fname, lname,  email, password));
            registrationViewModel.userRegistrationStatus.observe(RegistrationFragment.this, status -> {
                if(status.getStatus()){
                    Toast.makeText(getActivity(), status.getMsg(), Toast.LENGTH_SHORT).show();
                    getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new LoginFragment()).commit();
                    progressDialog.hide();
                }else {
                    Toast.makeText(getActivity(), status.getMsg(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}