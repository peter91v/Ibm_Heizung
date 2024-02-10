package com.example.ibm_heizung.ui.obergeschoss;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ObergeschossViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ObergeschossViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}