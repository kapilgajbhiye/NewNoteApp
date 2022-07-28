package com.example.mynewnoteapp.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mynewnoteapp.Model.Status;
import com.example.mynewnoteapp.Model.User;
import com.example.mynewnoteapp.Model.UserAuthService;

public class LoginViewModel extends ViewModel {
    private UserAuthService authService;
    private MutableLiveData<Status> _userLoginStatus = new MutableLiveData<>();
    public LiveData<Status> userLoginStatus = (LiveData<Status>)_userLoginStatus;

    private MutableLiveData<Status>_userResetPass = new MutableLiveData<>();
    public LiveData<Status> userResetPass = (LiveData<Status>)_userResetPass;

    public LoginViewModel(UserAuthService authService){
        this.authService = authService;
    }

    public void userLogin(User user){
        authService.loginUser(user, (status, msg) -> _userLoginStatus.setValue(new Status(status, msg)));
    }
    public void resetPassword(String email){
        authService.resetPassUser(email, ((status, msg) -> _userResetPass.setValue(new Status(status, msg))));
    }
}

