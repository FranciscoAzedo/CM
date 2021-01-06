package com.example.christmasapp.ui.pois;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PointsOfInterestViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PointsOfInterestViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is points of interest fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}