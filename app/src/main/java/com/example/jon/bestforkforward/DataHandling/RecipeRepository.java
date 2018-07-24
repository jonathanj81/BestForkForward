package com.example.jon.bestforkforward.DataHandling;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;

public class RecipeRepository {

    private RecipeDao mDao;
    private LiveData<List<Recipe>> mRecipes;

    public RecipeRepository(Context context) {
        RecipeDatabase db = RecipeDatabase.getDatabase(context);
        mDao = db.mDao();
        mRecipes = mDao.getAllRecipes();
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        return mRecipes;
    }
    public LiveData<Recipe> getSingleRecipe(int id){
        return mDao.getSingleRecipe(id);
    }

    public void updateRecipe(Recipe recipe){
        mDao.updateRecipe(recipe);
    }
    public void insertRecipe(Recipe recipe){ mDao.insertRecipe(recipe);}
}
