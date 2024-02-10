package com.example.ibm_heizung.ui.erdgeschoss;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ErgeschossViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ErgeschossViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}