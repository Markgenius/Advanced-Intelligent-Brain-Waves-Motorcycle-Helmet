package com.example.unicornmenu.ui.index;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class IndexViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public IndexViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is index fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}

