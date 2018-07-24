package com.example.jon.bestforkforward.DataHandling;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipe_table ORDER BY recipe_id")
    LiveData<List<Recipe>> getAllRecipes();

    @Query("SELECT * FROM recipe_table WHERE recipe_id = :recipe_id LIMIT 1")
    LiveData<Recipe> getSingleRecipe(int recipe_id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertRecipe(Recipe recipe);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecipe(Recipe recipe);

    @Query("SELECT COUNT(*) FROM recipe_table")
    int count();
}
