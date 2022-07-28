package com.example.mynewnoteapp.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<Boolean> _goToHomePageStatus = new MutableLiveData<>();
    public LiveData<Boolean> goToHomePageStatus = (LiveData<Boolean>) _goToHomePageStatus;

    private MutableLiveData<Boolean> _goToLoginPageStatus = new MutableLiveData<>();
    public LiveData<Boolean> goToLoginPageStatus = (LiveData<Boolean>) _goToLoginPageStatus;

    private MutableLiveData<Boolean> _goToRegistrationPageStatus = new MutableLiveData<>();
    public LiveData<Boolean> goToRegistrationPageStatus = (LiveData<Boolean>) _goToRegistrationPageStatus;

    public void set_goToHomePageStatus(Boolean status){
        this._goToHomePageStatus.setValue(status);

    }

    public void set_goToLoginPageStatus(Boolean status){
        this._goToLoginPageStatus.setValue(status);

    }

    public void set_goToRegistrationPageStatus(Boolean status){
        this._goToRegistrationPageStatus.setValue(status);

    }

}
