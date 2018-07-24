package com.example.jon.bestforkforward.DataHandling;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {

    private RecipeRepository mRepo;
    private LiveData<List<Recipe>> mRecipes;

    public RecipeViewModel(@NonNull Application application) {
        super(application);
        mRepo = new RecipeRepository(application);
        mRecipes = mRepo.getAllRecipes();
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        return mRecipes;
    }
}
