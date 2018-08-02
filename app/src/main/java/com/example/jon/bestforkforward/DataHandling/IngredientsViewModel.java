package com.example.jon.bestforkforward.DataHandling;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

public class IngredientsViewModel extends ViewModel {

    private LiveData<Recipe> mRecipe;
    private RecipeRepository mRepository;

    public IngredientsViewModel(Context context, int id) {
        mRepository = new RecipeRepository(context);
        mRecipe = mRepository.getSingleRecipe(id);
    }

    public LiveData<Recipe> getSingleRecipe() {
        return mRecipe;
    }
}
