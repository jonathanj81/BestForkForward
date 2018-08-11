package com.example.jon.bestforkforward.DataHandling;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.jon.bestforkforward.NetworkUtils.RetrofitFetcher;

import java.util.concurrent.Executors;

@Database(entities = {Recipe.class}, version = 2, exportSchema = false)
@TypeConverters(ListConverter.class)
public abstract class RecipeDatabase extends RoomDatabase {

    public abstract RecipeDao mDao();

    private static RecipeDatabase mDB;

    public synchronized static RecipeDatabase getDatabase(Context context) {
        if (mDB == null) {
            mDB = buildDatabase(context);
            mDB.populateData();
        }
        return mDB;
    }

    private static RecipeDatabase buildDatabase(final Context context) {

        return Room.databaseBuilder(context, RecipeDatabase.class, "recipes")
                .fallbackToDestructiveMigration()
                .build();
    }

    private void populateData() {
        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
            @Override
            public void run() {
                if (mDao().count() == 0) {
                    Recipe[] recipes = RetrofitFetcher.getData();
                    if (recipes != null && recipes.length > 0) {
                        for (Recipe recipe : recipes) {
                            mDao().insertRecipe(recipe);
                        }
                    }
                }
            }
        });
    }
}
