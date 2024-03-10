package com.example.unicornmenu.ui.brainwave_Fourier;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FourierViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public FourierViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
