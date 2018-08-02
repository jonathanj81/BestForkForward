package com.example.jon.bestforkforward.DataHandling;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

public class IngredientsViewModelFactory implements ViewModelProvider.Factory {
    private final Context mContext;
    private final int mID;

    public IngredientsViewModelFactory(Context context, int id) {
        mContext = context;
        mID = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new IngredientsViewModel(mContext,mID);
    }
}
