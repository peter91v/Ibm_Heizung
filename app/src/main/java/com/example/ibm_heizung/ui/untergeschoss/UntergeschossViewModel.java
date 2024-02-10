package com.example.ibm_heizung.ui.untergeschoss;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UntergeschossViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public UntergeschossViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}