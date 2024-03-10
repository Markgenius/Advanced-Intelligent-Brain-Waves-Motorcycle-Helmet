package com.example.unicornmenu.ui.brainwave;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BrainwaveViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public BrainwaveViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is brainwave fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}